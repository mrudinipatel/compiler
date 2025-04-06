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
*  processing function: gcd
*  jump around function body here
12: ST 0,-1(5)	store return
* -> compound statement
* -> if
* -> binary operation
* -> variable node
* processing variable: v
* <- variable node
13: ST 0,-2(5)	op: push left
14: LD 1,-2(5)	op: load left
15: JEQ 0,1(0)	op: eq
* <- binary operation
* -> return
* -> variable node
* processing variable: u
* <- variable node
17: LD 7,-1(5)	return to caller
* <- return
16: JEQ 0,1(7)	if: jump to else part
* <- if
* <- compound statement
18: LD 7,-1(5)	return caller
11: LDA 7,7(7)	jump around function body
* <- function declaration
* -> funnction declaration
*  processing function: main
*  jump around function body here
20: ST 0,-1(5)	store return
* -> compound statement
* -> operation
* -> variable node
* processing variable: x
* <- variable node
21: LD 1,-2(5)	op: load left
22: ST 0,0(1)	assign: store value
* <- operation
* -> operation
* -> variable node
* processing variable: y
* <- variable node
23: LD 1,-2(5)	op: load left
24: ST 0,0(1)	assign: store value
* <- operation
* -> call
* call of function: output
* -> call
* call of function: gcd
* -> variable node
* processing variable: x
* <- variable node
25: ST 0,-4(5)	op: push left
* -> variable node
* processing variable: y
* <- variable node
26: ST 0,-5(5)	op: push left
27: ST 5,-2(5)	push old frame pointer
28: LDA 5,-2(5)	push frame
29: LDA 0,1(7)	load ac with ret pointer
30: LDA 7,-31(7)	jump to function
31: LD 5,0(5)	pop frame
* <- call
32: ST 0,-4(5)	op: push left
33: ST 5,-2(5)	push old frame pointer
34: LDA 5,-2(5)	push frame
35: LDA 0,1(7)	load ac with ret pointer
36: LDA 7,-37(7)	jump to function
37: LD 5,0(5)	pop frame
* <- call
* <- compound statement
38: LD 7,-1(5)	return caller
19: LDA 7,19(7)	jump around function body
* <- function declaration
* Processing main function:
* End of execution
39: HALT 0,0,0	
