#!/usr/bin/python3

import os
import filecmp
import sys

sys.stdout = open('stdout.txt', 'w')


def cmp_lines(path_1, path_2, prnt=False):
    with open(path_1, 'r') as f1, open(path_2, 'r') as f2:
        l1 = f1.readline().strip("\n")
        l2 = f2.readline().strip("\n")
        if prnt:
            print(str(l1))
            print(l2)
        if l1 != l2:
            return False
    return True


dirs = ["TESTS1", "TESTS2", "TESTS3"]

# clear actuals
print("Clearing actuals")
for d in dirs:
    for filename in os.listdir(d):
        if filename.startswith("actual_"):
            os.remove(os.path.join(d, filename))


counter = 0
total_err = 0


for d in dirs:
    for filename in os.listdir(d):
        if filename.startswith("test_"):
            counter += 1
            if counter % 20 == 0:
                print("testing... ({})".format(counter))
            res = os.system("java -jar COMPILER {d}/{infname} {d}/actual_{outfname}"
                            .format(d=d, infname=filename, outfname=filename[5:]))
            if not cmp_lines(d + "/actual_" + filename[5:], d + "/expected_" + filename[5:]):
                total_err += 1
                print(">>>Error found in {}/{}".format(d, filename))
                cmp_lines(d + "/actual_" + filename[5:], d + "/expected_" + filename[5:], True)


if total_err == 0:
    print("------------All good mate------------")
else:
    print("Num of errors: {}".format(total_err))
