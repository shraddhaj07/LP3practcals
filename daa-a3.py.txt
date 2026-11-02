
# Fractional Knapsack using Greedy Method

def knapsack(weights, values, capacity):
    items = []

    for i in range(len(weights)):
        ratio = values[i] / weights[i]
        items.append((ratio, weights[i], values[i]))

    items.sort(reverse=True)
    total_value = 0

    for ratio, weight, value in items:
        if capacity == 0:
            break

        if weight <= capacity:
            capacity -= weight
            total_value += value
        else:
            total_value += ratio * capacity
            capacity = 0

    return total_value


n = int(input("Enter number of items: "))
weights = []
values = []

for i in range(n):
    w = float(input(f"Enter weight of item {i+1}: "))
    v = float(input(f"Enter value of item {i+1}: "))
    weights.append(w)
    values.append(v)

capacity = float(input("Enter knapsack capacity: "))

result = knapsack(weights, values, capacity)
print("Maximum value in Knapsack:", round(result, 2))
