make || { echo 'Make failed, exiting...'; exit; }
mkdir -p 'output'
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'
for arg in "$@"; do
	case $arg in
		--all)
			all=1
			;;
		--ast)
			ast=1
			;;
		*)
			echo "Unknown argument: $arg"
			exit
			;;
	esac
done
for file in $(ls input); do
	java -jar COMPILER input/"$file" output/"$file" &> /dev/null
	if cmp -s output/"$file" expected_output/"$file"; then
		[[ $all -eq 1 ]] && { echo "Running test: $file" ; echo -e "Result: ${GREEN}PASS${NC}"; echo; }
	else
		echo "Running test: $file"
		echo -e "Result: ${RED}FAIL${NC}"
		echo "Expected: $(cat expected_output/$file), Found: $(cat output/$file)"
		echo "Input: $(cat input/$file)"
		if [[ $ast -eq 1 ]] && [[ $(cat output/$file) == "OK" ]]; then
			mkdir -p output_ast
			dot -Tjpeg -ooutput_ast/${file%%.*}.jpeg \
						FOLDER_5_OUTPUT/AST_IN_GRAPHVIZ_DOT_FORMAT.txt
		fi
		echo
	fi
done
