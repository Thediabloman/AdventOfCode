from datetime import datetime
from datetime import timedelta

def readInput():
	file = open("Day2.txt", "r")
	input = file.read()
	file.close()
	lines = [int(i) for i in input.split(",")]
	return lines

def intcode(lines):
	i = 0
	while (not lines[i] == 99):
		#print(lines[i], lines[i+1], lines[i+2], lines[i+3], lines[lines[i+1]], lines[lines[i+2]])
		if lines[i] == 1:
			lines[lines[i+3]] = lines[lines[i+1]] + lines[lines[i+2]]
		if lines[i] == 2:
			lines[lines[i+3]] = lines[lines[i+1]] * lines[lines[i+2]]
		i += 4
	return lines[0]
	
def puzzle1():
	lines = readInput()
	lines[1] = 12
	lines[2] = 2
	return intcode(lines)
	
def puzzle2():
	for j in range(100):
		for l in range(100):
			lines = readInput()
			lines[1] = j
			lines[2] = l
			result = intcode(lines)
			#print(j, l, lines[0])
			
			if result == 19690720:
				return 100*j + l
	
	
######### PUZZLE 2 #########
print("Day 2:", puzzle1(), puzzle2())
