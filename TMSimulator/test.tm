* Standard prelude:
0: LD 6,0(0)	load gp with maxaddress
1: LDA 5,0(6)	copy gp to fp
2: ST 0,0(0)	clear at location 0
* Jump around i/o routines here
* code for input routine
4: ST 0,-1(5)	store return
5: IN 0,0,0	input
6: LD 7,-1(5)	return to caller
* code for output routine
7: ST 0,-1(5)	store return
8: LD 0,-2(5)	load output value
9: OUT 0,0,0	output
10: LD 7,-1(5)	return to caller
3: LDA 7,11(7)	jump around i/o routines
* End of standard prelude
* -> funnction declaration
*  processing function: main
*  jump around function body here
12: ST 0,-1(5)	store return
* -> compound statement
* -> operation
* -> variable node
* processing variable: fac
* <- variable node
13: LD 1,-2(5)	op: load left
14: ST 0,0(1)	assign: store value
* <- operation
* -> while
* while: jump after body comes back here
* -> binary operation
* -> variable node
* processing variable: x
* <- variable node
15: ST 0,-2(5)	op: push left
16: LD 1,-2(5)	op: load left
17: JGT 0,1(0)	op: gt
* <- binary operation
* -> compound statement
* -> operation
* -> variable node
* processing variable: fac
* <- variable node
19: LD 1,-2(5)	op: load left
20: ST 0,0(1)	assign: store value
* <- operation
* -> operation
* -> variable node
* processing variable: x
* <- variable node
21: LD 1,-2(5)	op: load left
22: ST 0,0(1)	assign: store value
* <- operation
* <- compound statement
23: LDA 7,-9(7)	while: absolute jump to test
18: JEQ 0,5(7)	while: jump to end
* <- while
* -> call
* call of function: output
* -> variable node
* processing variable: fac
* <- variable node
24: ST 0,-4(5)	op: push left
25: ST 5,-2(5)	push old frame pointer
26: LDA 5,-2(5)	push frame
27: LDA 0,1(7)	load ac with ret pointer
28: LDA 7,-29(7)	jump to function
29: LD 5,0(5)	pop frame
* <- call
* <- compound statement
30: LD 7,-1(5)	return caller
11: LDA 7,19(7)	jump around function body
* <- function declaration
* Processing main function:
* End of execution
31: HALT 0,0,0	
