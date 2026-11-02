
# DAA: Fibonacci using Recursive and Non-Recursive Methods

# Non-recursive (Iterative) method
def fibonacci_iterative(n):
    a, b = 0, 1
    steps = 0

    for i in range(n):
        print(a, end=" ")
        a, b = b, a + b
        steps += 1

    return steps

# Recursive method
steps_recursive = 0

def fibonacci_recursive(n):
    global steps_recursive
    steps_recursive += 1

    if n <= 1:
        return n
    return fibonacci_recursive(n - 1) + fibonacci_recursive(n - 2)


n = int(input("Enter the number of terms: "))

if n <= 0:
    print("Please enter a positive integer.")
else:
    print("\nFibonacci using Non-Recursive Method:")
    steps_iterative = fibonacci_iterative(n)

    print("\n\nFibonacci using Recursive Method:")
    for i in range(n):
        print(fibonacci_recursive(i), end=" ")

    print("\n\nStep Count:")
    print("Non-Recursive:", steps_iterative)
    print("Recursive:", steps_recursive)
