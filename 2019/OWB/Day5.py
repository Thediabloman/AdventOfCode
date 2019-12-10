from datetime import datetime
from datetime import timedelta

def readInput():
    file = open("Day5.txt", "r")
    input = file.read()
    file.close()
    lines = [int(i) for i in input.split(",")]
    print(len(lines), "lines.")
    return lines

def intcode(lines, input):
    print("Input:", input)
    i = 0
    output = 0
    while (not lines[i] == 99):
        opcode = str(lines[i]).zfill(5)
        #print(i, opcode, lines[i+1], lines[i+2], lines[i+3])
        
        # For all functions with one parameter
        v1 = lines[i+1] if int(opcode[2]) else lines[lines[i+1]]
        
        if int(opcode[-2:]) == 4:
            output = v1
            print("Out:", output)
            i += 2
            continue

        if int(opcode[-2:]) == 3:
            lines[lines[i+1]] = input
            i += 2
            continue
        
        # For all functions with two parameters
        v2 = lines[i+2] if int(opcode[1]) else lines[lines[i+2]]
        
        if int(opcode[-2:]) == 6:
            if not v1:
                i = v2
            else:
                i += 3
            continue
        
        if int(opcode[-2:]) == 5:
            if v1:
                i = v2
            else:
                i += 3
            continue              

        # For all functions with three parameters
        v3 = lines[i+3]
        
        if int(opcode[-2:]) == 8:        
            lines[v3] = 1 if v1 == v2 else 0
        
        if int(opcode[-2:]) == 7:
            lines[v3] = 1 if v1 < v2 else 0
            
        if int(opcode[-2:]) == 1:
            lines[v3]  = v1 + v2
            
        if int(opcode[-2:]) == 2:
            lines[v3]  = v1 * v2
        
        i += 4
    return output
    
def puzzle1():
    lines = readInput()
    return intcode(lines, 1)
    
def puzzle2():
    lines = readInput()
    return intcode(lines, 5)

def test():
    intcode(readInput(), 8)
    
######### PUZZLE 5 #########
print("Day 5:", puzzle1(), puzzle2())
#test()
