def readInput():
    filepath = 'Day4.txt'
    print()
    with open(filepath) as fp:
        n = fp.readline().strip().split('-')
        return (int(n[0]), int(n[1]))

def verify1(password):
    if not password == "".join(sorted(password)):
        return False
        
    return sum([password.count(n) >= 2 for n in password]) > 0 

def verify2(password):
    if not password == "".join(sorted(password)):
        return False
        
    return sum([password.count(n) == 2 for n in password]) > 0 

def puzzle1():
    (min, max) = readInput()
    count = sum([verify1(str(p)) for p  in range(min, max)])
    return count

def puzzle2():
    (min, max) = readInput()
    count = sum([verify2(str(p)) for p  in range(min, max)])
    return count

######### PUZZLE 4 #########
print("Day 4:", puzzle1(), puzzle2())
