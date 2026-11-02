
# Job Sequencing with Deadlines using Greedy Method

def job_sequencing(jobs):
    # Sort jobs by profit in descending order
    jobs.sort(key=lambda x: x[2], reverse=True)

    max_deadline = max(job[1] for job in jobs)
    slots = [False] * max_deadline
    result = [None] * max_deadline
    total_profit = 0

    for job in jobs:
        job_id, deadline, profit = job

        # Find available slot before deadline
        for j in range(deadline - 1, -1, -1):
            if not slots[j]:
                slots[j] = True
                result[j] = job_id
                total_profit += profit
                break

    print("Selected Jobs:", [job for job in result if job])
    print("Maximum Profit:", total_profit)


# Input
n = int(input("Enter number of jobs: "))
jobs = []

for i in range(n):
    job_id = input("Enter Job ID: ")
    deadline = int(input("Enter Deadline: "))
    profit = int(input("Enter Profit: "))
    jobs.append((job_id, deadline, profit))

job_sequencing(jobs)
