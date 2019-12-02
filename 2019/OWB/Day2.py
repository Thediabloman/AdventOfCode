def readInput():
	file = open("Day2.txt", "r")
	input = file.read()
	file.close()
	lines = [int(i) for i in input.split(",")]
	return lines
	
def puzzle1():
	lines = readInput()
	lines[1] = 12
	lines[2] = 2
	i = 0
	while (not lines[i] == 99):
		#print(lines[i], lines[i+1], lines[i+2], lines[i+3], lines[lines[i+1]], lines[lines[i+2]])
		if lines[i] == 1:
			lines[lines[i+3]] = lines[lines[i+1]] + lines[lines[i+2]]
		if lines[i] == 2:
			lines[lines[i+3]] = lines[lines[i+1]] * lines[lines[i+2]]
		if not (lines[i] == 1 or lines[i] == 2):
			print("########### Error: ",lines[i], " ############")
		i += 4
	return lines[0]
	
def puzzle2():
	for j in range(100):
		for l in range(100):
			lines = readInput()
			lines[1] = j
			lines[2] = l
			i = 0
			while (not lines[i] == 99):
				#print(lines[i], lines[i+1], lines[i+2], lines[i+3], lines[lines[i+1]], lines[lines[i+2]])
				if lines[i] == 1:
					lines[lines[i+3]] = lines[lines[i+1]] + lines[lines[i+2]]
				if lines[i] == 2:
					lines[lines[i+3]] = lines[lines[i+1]] * lines[lines[i+2]]
				if not (lines[i] == 1 or lines[i] == 2):
					print("########### Error: ",lines[i], " ############")
				i += 4
			print(j, l, lines[0])
			
			if lines[0] == 19690720:
				return 100*j + l
	
	
######### PUZZLE 2 #########
print("\n***** Day 2 *****")
print(puzzle1())
print(puzzle2())
