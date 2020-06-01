.data
string_illegal_div_by_0: .asciiz "Division By Zero"
string_access_violation: .asciiz "Access Violation"
string_invalid_ptr_dref: .asciiz "Invalid Pointer Dereference"
func_init: .asciiz "init"
func_main: .asciiz "main"
string_0: .asciiz "HELLO"
string_1: .asciiz "BYE"
Label_8_C_vTable: .word Label_11_init
Label_3_B_vTable: .word Label_6_init
Label_4_B_var_init: .word 0
Label_1_A_var_init: .word string_0
Label_9_C_var_init: .word 0
.text
PrintInt:
	lw $a0,4($fp)
	li $v0,1
	syscall
	li $a0,32
	li $v0,11
	syscall
	jr $ra
PrintString:
	lw $a0,4($fp)
	li $v0,4
	syscall
	jr $ra
Label_2_A_class_init:
	addi $sp,$sp,0
	li $t0,8
	move $a0,$t0
	li $v0,9
	syscall
	move $t0,$v0
	addi $t1, $t0, 4
	la $t2, Label_1_A_var_init
	lw $t2,0($t2)
	sw $t2,0($t1)
	move $v0,$t0
	j Label_2_A_class_init_end
Label_2_A_class_init_end:
	addi $sp,$sp,0
	jr $ra
Label_6_init:
	addi $sp,$sp,-8
	lw $t0, 4($fp)
	addi $t0, $t0, 4
	addi $t1,$fp,8
	lw $t1,0($t1)
	addi $t2, $t1, 1
	li $t3,4
	mul $t2,$t2,$t3
	move $a0,$t2
	li $v0,9
	syscall
	move $t2,$v0
	sw $t1,0($t2)
	sw $t2,0($t0)
	addi $t0,$fp,-8
	addi $t1,$fp,8
	lw $t1,0($t1)
	li $t2,1
	sub $t1,$t1,$t2
	li $t2,-32767
	blt $t1,$t2,Label_32_UNDERFLOW
	li $t2,32767
	blt $t2,$t1,Label_33_OVERFLOW
	j Label_34_SUB_END
Label_32_UNDERFLOW:
	li $t1,-32768
	j Label_34_SUB_END
Label_33_OVERFLOW:
	li $t1,32767
Label_34_SUB_END:
	sw $t1,0($t0)
Label_15_WHILE_START:
	addi $t0,$fp,-8
	lw $t0,0($t0)
	beq $t0,$zero,Label_14_WHILE_END
	addi $t0,$fp,-8
	addi $t1,$fp,-8
	lw $t1,0($t1)
	li $t2,1
	sub $t1,$t1,$t2
	li $t2,-32767
	blt $t1,$t2,Label_35_UNDERFLOW
	li $t2,32767
	blt $t2,$t1,Label_36_OVERFLOW
	j Label_37_SUB_END
Label_35_UNDERFLOW:
	li $t1,-32768
	j Label_37_SUB_END
Label_36_OVERFLOW:
	li $t1,32767
Label_37_SUB_END:
	sw $t1,0($t0)
	j Label_15_WHILE_START
Label_14_WHILE_END:
Label_7_init_end:
	addi $sp,$sp,8
	jr $ra
Label_5_B_class_init:
	addi $sp,$sp,0
	li $t0,8
	move $a0,$t0
	li $v0,9
	syscall
	move $t0,$v0
	la $t1, Label_3_B_vTable
	sw $t1,0($t0)
	addi $t1, $t0, 4
	la $t2, Label_4_B_var_init
	lw $t2,0($t2)
	sw $t2,0($t1)
	move $v0,$t0
	j Label_5_B_class_init_end
Label_5_B_class_init_end:
	addi $sp,$sp,0
	jr $ra
Label_11_init:
	addi $sp,$sp,-8
	lw $t0, 4($fp)
	addi $t0, $t0, 4
	addi $t1,$fp,8
	lw $t1,0($t1)
	addi $t2, $t1, 1
	li $t3,4
	mul $t2,$t2,$t3
	move $a0,$t2
	li $v0,9
	syscall
	move $t2,$v0
	sw $t1,0($t2)
	sw $t2,0($t0)
	addi $t0,$fp,-8
	addi $t1,$fp,8
	lw $t1,0($t1)
	li $t2,1
	sub $t1,$t1,$t2
	li $t2,-32767
	blt $t1,$t2,Label_38_UNDERFLOW
	li $t2,32767
	blt $t2,$t1,Label_39_OVERFLOW
	j Label_40_SUB_END
Label_38_UNDERFLOW:
	li $t1,-32768
	j Label_40_SUB_END
Label_39_OVERFLOW:
	li $t1,32767
Label_40_SUB_END:
	sw $t1,0($t0)
Label_17_WHILE_START:
	addi $t0,$fp,-8
	lw $t0,0($t0)
	beq $t0,$zero,Label_16_WHILE_END
	addi $t0,$fp,-8
	lw $t0,0($t0)
	lw $t1, 4($fp)
	addi $t1, $t1, 4
	lw $t1,0($t1)
	bne $t1,$zero,Label_41_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_41_Not_Nil:
	lw $t2,0($t1)
	blt $t0,$t2,Label_19_Not_Overlow
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_19_Not_Overlow:
	li $t2,0
	ble $t2,$t0,Label_18_Legal_index
	blt $t2,$t0,Label_18_Legal_index
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_18_Legal_index:
	addi $t0, $t0, 1
	li $t2,4
	mul $t0,$t0,$t2
	add $t0,$t0,$t1
	addi $sp,$sp,-40
	sw $fp,0($sp)
	sw $ra,4($sp)
	sw $t0,8($sp)
	sw $t1,12($sp)
	sw $t2,16($sp)
	sw $t3,20($sp)
	sw $t4,24($sp)
	sw $t5,28($sp)
	sw $t6,32($sp)
	sw $t7,36($sp)
	addi $fp,$sp,0
	jal Label_2_A_class_init
	lw $ra,4($fp)
	lw $t0,8($fp)
	lw $t1,12($fp)
	lw $t2,16($fp)
	lw $t3,20($fp)
	lw $t4,24($fp)
	lw $t5,28($fp)
	lw $t6,32($fp)
	lw $t7,36($fp)
	addi $sp,$fp,40
	lw $fp,0($fp)
	move $t1,$v0
	sw $t1,0($t0)
	addi $t0,$fp,-8
	lw $t0,0($t0)
	lw $t1, 4($fp)
	addi $t1, $t1, 4
	lw $t1,0($t1)
	bne $t1,$zero,Label_42_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_42_Not_Nil:
	lw $t2,0($t1)
	blt $t0,$t2,Label_21_Not_Overlow
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_21_Not_Overlow:
	li $t2,0
	ble $t2,$t0,Label_20_Legal_index
	blt $t2,$t0,Label_20_Legal_index
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_20_Legal_index:
	addi $t0, $t0, 1
	li $t2,4
	mul $t0,$t0,$t2
	add $t0,$t0,$t1
	lw $t0,0($t0)
	bne $t0,$zero,Label_43_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_43_Not_Nil:
	addi $t0, $t0, 4
	la $t1, string_1
	sw $t1,0($t0)
	addi $t0,$fp,-8
	addi $t1,$fp,-8
	lw $t1,0($t1)
	li $t2,1
	sub $t1,$t1,$t2
	li $t2,-32767
	blt $t1,$t2,Label_44_UNDERFLOW
	li $t2,32767
	blt $t2,$t1,Label_45_OVERFLOW
	j Label_46_SUB_END
Label_44_UNDERFLOW:
	li $t1,-32768
	j Label_46_SUB_END
Label_45_OVERFLOW:
	li $t1,32767
Label_46_SUB_END:
	sw $t1,0($t0)
	j Label_17_WHILE_START
Label_16_WHILE_END:
Label_12_init_end:
	addi $sp,$sp,8
	jr $ra
Label_10_C_class_init:
	addi $sp,$sp,0
	li $t0,8
	move $a0,$t0
	li $v0,9
	syscall
	move $t0,$v0
	la $t1, Label_8_C_vTable
	sw $t1,0($t0)
	addi $t1, $t0, 4
	la $t2, Label_9_C_var_init
	lw $t2,0($t2)
	sw $t2,0($t1)
	move $v0,$t0
	j Label_10_C_class_init_end
Label_10_C_class_init_end:
	addi $sp,$sp,0
	jr $ra
our_main:
	addi $fp,$sp,0
	sw $zero,0($fp)
	addi $sp,$sp,-12
	addi $t0,$fp,-8
	addi $sp,$sp,-40
	sw $fp,0($sp)
	sw $ra,4($sp)
	sw $t0,8($sp)
	sw $t1,12($sp)
	sw $t2,16($sp)
	sw $t3,20($sp)
	sw $t4,24($sp)
	sw $t5,28($sp)
	sw $t6,32($sp)
	sw $t7,36($sp)
	addi $fp,$sp,0
	jal Label_5_B_class_init
	lw $ra,4($fp)
	lw $t0,8($fp)
	lw $t1,12($fp)
	lw $t2,16($fp)
	lw $t3,20($fp)
	lw $t4,24($fp)
	lw $t5,28($fp)
	lw $t6,32($fp)
	lw $t7,36($fp)
	addi $sp,$fp,40
	lw $fp,0($fp)
	move $t1,$v0
	sw $t1,0($t0)
	addi $t0,$fp,-12
	addi $sp,$sp,-40
	sw $fp,0($sp)
	sw $ra,4($sp)
	sw $t0,8($sp)
	sw $t1,12($sp)
	sw $t2,16($sp)
	sw $t3,20($sp)
	sw $t4,24($sp)
	sw $t5,28($sp)
	sw $t6,32($sp)
	sw $t7,36($sp)
	addi $fp,$sp,0
	jal Label_10_C_class_init
	lw $ra,4($fp)
	lw $t0,8($fp)
	lw $t1,12($fp)
	lw $t2,16($fp)
	lw $t3,20($fp)
	lw $t4,24($fp)
	lw $t5,28($fp)
	lw $t6,32($fp)
	lw $t7,36($fp)
	addi $sp,$fp,40
	lw $fp,0($fp)
	move $t1,$v0
	sw $t1,0($t0)
	addi $sp,$sp,-44
	sw $fp,0($sp)
	la $t0,PrintInt
	sw $t0,-4($fp)
	li $t0,560
	sw $t0,4($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	addi $fp,$sp,0
	jal PrintInt
	lw $ra,8($fp)
	lw $t0,12($fp)
	lw $t1,16($fp)
	lw $t2,20($fp)
	lw $t3,24($fp)
	lw $t4,28($fp)
	lw $t5,32($fp)
	lw $t6,36($fp)
	lw $t7,40($fp)
	addi $sp,$fp,44
	lw $fp,0($fp)
	addi $sp,$sp,-48
	sw $fp,0($sp)
	la $t0,func_init
	sw $t0,-4($fp)
	addi $t0,$fp,-8
	lw $t0,0($t0)
	bne $t0,$zero,Label_47_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_47_Not_Nil:
	sw $t0,4($sp)
	li $t0,80
	sw $t0,8($sp)
	sw $ra,12($sp)
	sw $t0,16($sp)
	sw $t1,20($sp)
	sw $t2,24($sp)
	sw $t3,28($sp)
	sw $t4,32($sp)
	sw $t5,36($sp)
	sw $t6,40($sp)
	sw $t7,44($sp)
	sw $ra,12($sp)
	sw $t0,16($sp)
	sw $t1,20($sp)
	sw $t2,24($sp)
	sw $t3,28($sp)
	sw $t4,32($sp)
	sw $t5,36($sp)
	sw $t6,40($sp)
	sw $t7,44($sp)
	addi $fp,$sp,0
	lw $t0, 4($fp)
	lw $t0,0($t0)
	lw $t0,0($t0)
	jal $t0
	lw $ra,12($fp)
	lw $t0,16($fp)
	lw $t1,20($fp)
	lw $t2,24($fp)
	lw $t3,28($fp)
	lw $t4,32($fp)
	lw $t5,36($fp)
	lw $t6,40($fp)
	lw $t7,44($fp)
	addi $sp,$fp,48
	lw $fp,0($fp)
	addi $sp,$sp,-48
	sw $fp,0($sp)
	la $t0,func_init
	sw $t0,-4($fp)
	addi $t0,$fp,-12
	lw $t0,0($t0)
	bne $t0,$zero,Label_48_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_48_Not_Nil:
	sw $t0,4($sp)
	li $t0,10
	sw $t0,8($sp)
	sw $ra,12($sp)
	sw $t0,16($sp)
	sw $t1,20($sp)
	sw $t2,24($sp)
	sw $t3,28($sp)
	sw $t4,32($sp)
	sw $t5,36($sp)
	sw $t6,40($sp)
	sw $t7,44($sp)
	sw $ra,12($sp)
	sw $t0,16($sp)
	sw $t1,20($sp)
	sw $t2,24($sp)
	sw $t3,28($sp)
	sw $t4,32($sp)
	sw $t5,36($sp)
	sw $t6,40($sp)
	sw $t7,44($sp)
	addi $fp,$sp,0
	lw $t0, 4($fp)
	lw $t0,0($t0)
	lw $t0,0($t0)
	jal $t0
	lw $ra,12($fp)
	lw $t0,16($fp)
	lw $t1,20($fp)
	lw $t2,24($fp)
	lw $t3,28($fp)
	lw $t4,32($fp)
	lw $t5,36($fp)
	lw $t6,40($fp)
	lw $t7,44($fp)
	addi $sp,$fp,48
	lw $fp,0($fp)
	addi $sp,$sp,-44
	sw $fp,0($sp)
	la $t0,PrintString
	sw $t0,-4($fp)
	li $t0,0
	addi $t1,$fp,-8
	lw $t1,0($t1)
	bne $t1,$zero,Label_49_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_49_Not_Nil:
	addi $t1, $t1, 4
	lw $t1,0($t1)
	bne $t1,$zero,Label_50_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_50_Not_Nil:
	lw $t2,0($t1)
	blt $t0,$t2,Label_23_Not_Overlow
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_23_Not_Overlow:
	li $t2,0
	ble $t2,$t0,Label_22_Legal_index
	blt $t2,$t0,Label_22_Legal_index
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_22_Legal_index:
	addi $t0, $t0, 1
	li $t2,4
	mul $t0,$t0,$t2
	add $t0,$t0,$t1
	lw $t0,0($t0)
	bne $t0,$zero,Label_51_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_51_Not_Nil:
	addi $t0, $t0, 4
	lw $t0,0($t0)
	sw $t0,4($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	addi $fp,$sp,0
	jal PrintString
	lw $ra,8($fp)
	lw $t0,12($fp)
	lw $t1,16($fp)
	lw $t2,20($fp)
	lw $t3,24($fp)
	lw $t4,28($fp)
	lw $t5,32($fp)
	lw $t6,36($fp)
	lw $t7,40($fp)
	addi $sp,$fp,44
	lw $fp,0($fp)
	addi $sp,$sp,-44
	sw $fp,0($sp)
	la $t0,PrintString
	sw $t0,-4($fp)
	li $t0,1
	addi $t1,$fp,-8
	lw $t1,0($t1)
	bne $t1,$zero,Label_52_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_52_Not_Nil:
	addi $t1, $t1, 4
	lw $t1,0($t1)
	bne $t1,$zero,Label_53_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_53_Not_Nil:
	lw $t2,0($t1)
	blt $t0,$t2,Label_25_Not_Overlow
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_25_Not_Overlow:
	li $t2,0
	ble $t2,$t0,Label_24_Legal_index
	blt $t2,$t0,Label_24_Legal_index
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_24_Legal_index:
	addi $t0, $t0, 1
	li $t2,4
	mul $t0,$t0,$t2
	add $t0,$t0,$t1
	lw $t0,0($t0)
	bne $t0,$zero,Label_54_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_54_Not_Nil:
	addi $t0, $t0, 4
	lw $t0,0($t0)
	sw $t0,4($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	addi $fp,$sp,0
	jal PrintString
	lw $ra,8($fp)
	lw $t0,12($fp)
	lw $t1,16($fp)
	lw $t2,20($fp)
	lw $t3,24($fp)
	lw $t4,28($fp)
	lw $t5,32($fp)
	lw $t6,36($fp)
	lw $t7,40($fp)
	addi $sp,$fp,44
	lw $fp,0($fp)
	addi $sp,$sp,-44
	sw $fp,0($sp)
	la $t0,PrintString
	sw $t0,-4($fp)
	li $t0,1
	addi $t1,$fp,-12
	lw $t1,0($t1)
	bne $t1,$zero,Label_55_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_55_Not_Nil:
	addi $t1, $t1, 4
	lw $t1,0($t1)
	bne $t1,$zero,Label_56_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_56_Not_Nil:
	lw $t2,0($t1)
	blt $t0,$t2,Label_27_Not_Overlow
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_27_Not_Overlow:
	li $t2,0
	ble $t2,$t0,Label_26_Legal_index
	blt $t2,$t0,Label_26_Legal_index
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_26_Legal_index:
	addi $t0, $t0, 1
	li $t2,4
	mul $t0,$t0,$t2
	add $t0,$t0,$t1
	lw $t0,0($t0)
	bne $t0,$zero,Label_57_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_57_Not_Nil:
	addi $t0, $t0, 4
	lw $t0,0($t0)
	sw $t0,4($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	addi $fp,$sp,0
	jal PrintString
	lw $ra,8($fp)
	lw $t0,12($fp)
	lw $t1,16($fp)
	lw $t2,20($fp)
	lw $t3,24($fp)
	lw $t4,28($fp)
	lw $t5,32($fp)
	lw $t6,36($fp)
	lw $t7,40($fp)
	addi $sp,$fp,44
	lw $fp,0($fp)
	addi $sp,$sp,-44
	sw $fp,0($sp)
	la $t0,PrintString
	sw $t0,-4($fp)
	li $t0,4
	addi $t1,$fp,-12
	lw $t1,0($t1)
	bne $t1,$zero,Label_58_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_58_Not_Nil:
	addi $t1, $t1, 4
	lw $t1,0($t1)
	bne $t1,$zero,Label_59_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_59_Not_Nil:
	lw $t2,0($t1)
	blt $t0,$t2,Label_29_Not_Overlow
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_29_Not_Overlow:
	li $t2,0
	ble $t2,$t0,Label_28_Legal_index
	blt $t2,$t0,Label_28_Legal_index
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_28_Legal_index:
	addi $t0, $t0, 1
	li $t2,4
	mul $t0,$t0,$t2
	add $t0,$t0,$t1
	lw $t0,0($t0)
	bne $t0,$zero,Label_60_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_60_Not_Nil:
	addi $t0, $t0, 4
	lw $t0,0($t0)
	sw $t0,4($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	addi $fp,$sp,0
	jal PrintString
	lw $ra,8($fp)
	lw $t0,12($fp)
	lw $t1,16($fp)
	lw $t2,20($fp)
	lw $t3,24($fp)
	lw $t4,28($fp)
	lw $t5,32($fp)
	lw $t6,36($fp)
	lw $t7,40($fp)
	addi $sp,$fp,44
	lw $fp,0($fp)
	addi $sp,$sp,-44
	sw $fp,0($sp)
	la $t0,PrintString
	sw $t0,-4($fp)
	li $t0,10
	addi $t1,$fp,-12
	lw $t1,0($t1)
	bne $t1,$zero,Label_61_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_61_Not_Nil:
	addi $t1, $t1, 4
	lw $t1,0($t1)
	bne $t1,$zero,Label_62_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_62_Not_Nil:
	lw $t2,0($t1)
	blt $t0,$t2,Label_31_Not_Overlow
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_31_Not_Overlow:
	li $t2,0
	ble $t2,$t0,Label_30_Legal_index
	blt $t2,$t0,Label_30_Legal_index
	la $a0,string_access_violation
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_30_Legal_index:
	addi $t0, $t0, 1
	li $t2,4
	mul $t0,$t0,$t2
	add $t0,$t0,$t1
	lw $t0,0($t0)
	bne $t0,$zero,Label_63_Not_Nil
	la $a0,string_invalid_ptr_dref
	li $v0,4
	syscall
	li $v0,10
	syscall
Label_63_Not_Nil:
	addi $t0, $t0, 4
	lw $t0,0($t0)
	sw $t0,4($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	sw $ra,8($sp)
	sw $t0,12($sp)
	sw $t1,16($sp)
	sw $t2,20($sp)
	sw $t3,24($sp)
	sw $t4,28($sp)
	sw $t5,32($sp)
	sw $t6,36($sp)
	sw $t7,40($sp)
	addi $fp,$sp,0
	jal PrintString
	lw $ra,8($fp)
	lw $t0,12($fp)
	lw $t1,16($fp)
	lw $t2,20($fp)
	lw $t3,24($fp)
	lw $t4,28($fp)
	lw $t5,32($fp)
	lw $t6,36($fp)
	lw $t7,40($fp)
	addi $sp,$fp,44
	lw $fp,0($fp)
Label_13_main_end:
	jr $ra
main:
	jal our_main
	li $v0,10
	syscall
