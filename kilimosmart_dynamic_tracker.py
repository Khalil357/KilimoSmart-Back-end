# kilimosmart_dynamic_tracker.py

import os
import subprocess

def count_files(folder_path, extensions=None):
    """
    Count total files in folder_path.
    If extensions is given, count only files with those extensions.
    """
    total_files = 0
    for root, _, files in os.walk(folder_path):
        for file in files:
            if extensions:
                if any(file.endswith(ext) for ext in extensions):
                    total_files += 1
            else:
                total_files += 1
    return total_files

def count_todos(folder_path):
    """
    Count TODO/FIXME comments in code as remaining tasks.
    """
    todo_count = 0
    for root, _, files in os.walk(folder_path):
        for file in files:
            if file.endswith((".js", ".jsx", ".ts", ".java", ".py")):
                try:
                    with open(os.path.join(root, file), "r", encoding="utf-8") as f:
                        for line in f:
                            if "TODO" in line or "FIXME" in line:
                                todo_count += 1
                except:
                    continue
    return todo_count

def git_commit_count(repo_path):
    """
    Count total commits in the repo.
    """
    try:
        result = subprocess.run(
            ["git", "-C", repo_path, "rev-list", "--count", "HEAD"],
            capture_output=True, text=True
        )
        return int(result.stdout.strip())
    except:
        return 0

def estimate_progress(repo_path, code_extensions=None):
    """
    Estimate progress based on files and TODOs.
    """
    total_files = count_files(repo_path, code_extensions)
    todos = count_todos(repo_path)
    commits = git_commit_count(repo_path)

    # Simple formula: more commits + fewer TODOs = higher progress
    if total_files == 0:
        progress = 0
    else:
        progress = ((total_files - todos) / total_files * 0.7 + min(commits/100, 0.3)) * 100
    return round(progress, 2), total_files, todos, commits

def generate_report(frontend_path, backend_path):
    print("📊 KilimoSmart Dynamic Progress Tracker\n")

    fe_progress, fe_files, fe_todos, fe_commits = estimate_progress(frontend_path, [".js", ".jsx", ".ts"])
    be_progress, be_files, be_todos, be_commits = estimate_progress(backend_path, [".java"])

    print(f"--- Frontend ({frontend_path}) ---")
    print(f"Total files: {fe_files}")
    print(f"TODOs/FIXMEs: {fe_todos}")
    print(f"Git commits: {fe_commits}")
    print(f"Estimated Progress: {fe_progress}%\n")

    print(f"--- Backend ({backend_path}) ---")
    print(f"Total files: {be_files}")
    print(f"TODOs/FIXMEs: {be_todos}")
    print(f"Git commits: {be_commits}")
    print(f"Estimated Progress: {be_progress}%\n")

    total_progress = round((fe_progress + be_progress) / 2, 2)
    print(f"✅ Overall Project Progress: {total_progress}%\n")

    # Save report to file
    with open("progress_report.txt", "w") as f:
        f.write("KilimoSmart Dynamic Progress Report\n\n")
        f.write(f"--- Frontend ({frontend_path}) ---\n")
        f.write(f"Total files: {fe_files}\nTODOs/FIXMEs: {fe_todos}\nGit commits: {fe_commits}\nEstimated Progress: {fe_progress}%\n\n")
        f.write(f"--- Backend ({backend_path}) ---\n")
        f.write(f"Total files: {be_files}\nTODOs/FIXMEs: {be_todos}\nGit commits: {be_commits}\nEstimated Progress: {be_progress}%\n\n")
        f.write(f"Overall Project Progress: {total_progress}%\n")
    print("📄 Report saved as progress_report.txt")

if __name__ == "__main__":
    frontend_repo_path = input("Enter frontend repo path: ").strip()
    backend_repo_path = input("Enter backend repo path: ").strip()
    generate_report(frontend_repo_path, backend_repo_path)
