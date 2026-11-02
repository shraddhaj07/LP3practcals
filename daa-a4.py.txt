
# 0-1 Knapsack using Dynamic Programming

def knapsack(weights, values, capacity):
    n = len(weights)

    dp = [[0] * (capacity + 1) for _ in range(n + 1)]

    for i in range(1, n + 1):
        for w in range(capacity + 1):
            if weights[i - 1] <= w:
                dp[i][w] = max(
                    values[i - 1] + dp[i - 1][w - weights[i - 1]],
                    dp[i - 1][w]
                )
            else:
                dp[i][w] = dp[i - 1][w]

    return dp[n][capacity]


n = int(input("Enter number of items: "))

weights = []
values = []

for i in range(n):
    w = int(input(f"Enter weight of item {i+1}: "))
    v = int(input(f"Enter value of item {i+1}: "))
    weights.append(w)
    values.append(v)

capacity = int(input("Enter knapsack capacity: "))

result = knapsack(weights, values, capacity)

print("Maximum value in Knapsack:", result)
