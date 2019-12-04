def readInput():
    filepath = 'Day4.txt'
    print()
    with open(filepath) as fp:
        n = fp.readline().strip().split('-')
        return (int(n[0]), int(n[1]))

def verify(password):
    adjacent = 0
    l = [int(n) for n in str(password)]
    for i in range(len(l)-1):
        if l[i+1] < l[i]:
            return False
        if l[i] == l[i+1]:
            adjacent = True
    return adjacent

def verify2(password):
    adjacent = False
    l = [int(n) for n in str(password)]
    innerAdjacent = 0
    for i in range(len(l)-1):
        if l[i+1] < l[i]:
            return False
        if l[i] == l[i+1]:
            adjacent = l.count(l[i]) == 2
    
    return adjacent

def puzzle1():
    (min, max) = readInput()
    count = sum([verify(p) for p  in range(min, max)])
    return count

def puzzle2():
    (min, max) = readInput()
    count = sum([verify2(p) for p  in range(min, max)])
    return count

print(puzzle1())
print(puzzle2())
