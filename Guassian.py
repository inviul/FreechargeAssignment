import numpy as np


def get5SignificantDigit(number, significantDigit):
    from sigfig import round
    return round(number, sigfigs=significantDigit)

suml = []
multil = []
divl = []

def forwardEliminationWithoutPivot(aMatrix, matrixSize, bMatrix, significantDigit):
    div = 0
    sum = 0
    mult = 0
    for diag in range(matrixSize - 1):
        for row in range(diag + 1, matrixSize):
            if aMatrix[row, diag] == 0:
                continue
            factor = get5SignificantDigit(aMatrix[row, diag] / aMatrix[diag, diag], significantDigit)
            div += 1
            for col in range(diag, matrixSize):
                multi = get5SignificantDigit(factor * aMatrix[diag, col], significantDigit)
                aMatrix[row, col] = get5SignificantDigit(aMatrix[row, col] - multi, significantDigit)
                sum += 1
                mult += 1
            multB = get5SignificantDigit(factor * bMatrix[diag], significantDigit)
            bMatrix[row] = get5SignificantDigit(bMatrix[row] - multB, significantDigit)
            sum += 1
            mult += 1
    # print("A after forward Elimination:\n", aMatrix)
    # print("\nB after forward Elimination:", bMatrix)
    print("FE- Sum: ", sum, ", Mult: ", mult, ", Div: ", div)
    suml.append(sum)
    divl.append(div)
    multil.append(mult)



def forwardEliminationWithPivot(aMatrix, matrixSize, bMatrix, significantDigit):
    div = 0
    sum = 0
    mult = 0
    for diag in range(matrixSize - 1):
        if np.fabs(a[diag, diag]) < 1.0e-12:
            for inloop in range(diag + 1, matrixSize):
                if np.fabs(a[inloop, diag]) > np.fabs(a[diag, diag]):
                    a[[diag, inloop]] = a[[inloop, diag]]
                    b[[diag, inloop]] = b[[inloop, diag]]
                    break
        for row in range(diag + 1, matrixSize):
            if aMatrix[row, diag] == 0:
                continue
            factor = get5SignificantDigit(aMatrix[row, diag] / aMatrix[diag, diag], significantDigit)
            div += 1
            for col in range(diag, matrixSize):
                multi = get5SignificantDigit(factor * aMatrix[diag, col], significantDigit)
                aMatrix[row, col] = get5SignificantDigit(aMatrix[row, col] - multi, significantDigit)
                sum += 1
                mult += 1
            multB = get5SignificantDigit(factor * bMatrix[diag], significantDigit)
            bMatrix[row] = get5SignificantDigit(bMatrix[row] - multB, significantDigit)
            sum += 1
            mult += 1
    # print("A after forward Elimination:\n", aMatrix)
    # print("\nB after forward Elimination:", bMatrix)
    print("FE- Sum: ", sum, ", Mult: ", mult, ", Div: ", div)
    suml.append(sum)
    divl.append(div)
    multil.append(mult)


def backwardSubstitution(zeroMatrix, bMatrix, aMatrix, matrixSize, significantDigit):
    di = 0
    su = 0
    mu = 0
    zeroMatrix[matrixSize - 1] = get5SignificantDigit(bMatrix[matrixSize - 1] / aMatrix[matrixSize - 1, matrixSize - 1],
                                             significantDigit)
    di += 1
    for row in range(matrixSize - 2, -1, -1):
        sum = 0
        for col in range(row + 1, matrixSize):
            sum += get5SignificantDigit(aMatrix[row, col] * zeroMatrix[col], significantDigit)
            mu += 1
            su += 1
        zeroMatrix[row] = get5SignificantDigit((bMatrix[row] - sum) / aMatrix[row, row], significantDigit)
        di += 1
    # print("\nSolution is: ", zeroMatrix)
    print("BS- Sum: ", su, ", Mult: ", mu, ", Div: ", di)
    suml.append(su)
    divl.append(di)
    multil.append(mu)


def generateMatrixWith5S(rowSize, colSize):
    from numpy import random, array
    from random import randint
    from sigfig import round
    myMatrix = []
    mat = random.rand(rowSize, colSize)
    for i in range(len(mat)):
        innerMatrix = []
        for j in mat[i]:
            num = round(randint(1, 4) + j, sigfigs=5)
            innerMatrix.append(num)
        myMatrix.append(innerMatrix)
    arr = array(myMatrix, dtype=float)
    return arr


def generateBVector5S(matrix, size):
    from sigfig import round
    from numpy import zeros
    b = zeros(size, float)
    for i in range(len(matrix)):
        b[i] = round(matrix[i][0], sigfigs=5)
    return b


# print(generateMatrixWith5S(4,5))
# print(generateBVector5S(generateMatrixWith5S(4, 1), 4))
# print(get5SignificantDigit(1.2345678, 5))

# a = np.array([[1, 2], [3, 4]], float)
# b = np.array([5, 6], float)
# a = np.array([[1, 2, 3, 4], [4, 5, 6, 7], [7, 8, 9, 10], [10, 11, 12, 13]], float)
# b = np.array([4, 5, 6, 7], float)
# n = len(b)
# x = np.zeros(n, float)


matrixSizeList = [x for x in range(100, 1100, 100)]
dS = 5
actualTime = []
from time import time
for i in matrixSizeList:
    print("Starting for ", i, "x", i, "Matrix...")
    a = generateMatrixWith5S(i, i)
    b = generateBVector5S(a, i)
    x = np.zeros(i, float)
    t1 = time()
    forwardEliminationWithoutPivot(a, i, b, dS)
    backwardSubstitution(x, b, a, i, dS)
    t2 = time()
    actualTime.append(t2-t1)

theoryTime = []
for j in matrixSizeList:
    print("Calculating theoritical time for ", i, "x", i, "Matrix...")
    t3 = time()
    multi = (j**3+j**2-10*j+8)/3
    multi_add = 2*multi
    div = (j**2+j)/2
    tot_time = multi_add+div
    t4 = time()
    theoryTime.append(t4 - t3)

print(actualTime)
print(theoryTime)

