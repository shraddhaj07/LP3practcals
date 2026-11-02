
# N-Queens Problem using Backtracking

def print_board(board, n):
    for i in range(n):
        for j in range(n):
            print("Q" if board[i][j] == 1 else ".", end=" ")
        print()

def is_safe(board, row, col, n):
    for i in range(row):
        if board[i][col] == 1:
            return False

    i, j = row - 1, col - 1
    while i >= 0 and j >= 0:
        if board[i][j] == 1:
            return False
        i -= 1
        j -= 1

    i, j = row - 1, col + 1
    while i >= 0 and j < n:
        if board[i][j] == 1:
            return False
        i -= 1
        j += 1

    return True

def solve_n_queens(board, row, n):
    if row == n:
        return True

    for col in range(n):
        if is_safe(board, row, col, n):
            board[row][col] = 1

            if solve_n_queens(board, row + 1, n):
                return True

            board[row][col] = 0

    return False

n = int(input("Enter the value of N: "))
board = [[0] * n for _ in range(n)]

if n <= 0:
    print("Enter a positive integer.")
else:
    # Place the first queen in the first column of row 0
    board[0][0] = 1

    if solve_n_queens(board, 1, n):
        print("\nN-Queens Matrix:")
        print_board(board, n)
    else:
        print("No solution exists with the first queen at (0, 0).")
