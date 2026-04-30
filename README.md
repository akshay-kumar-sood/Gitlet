🚀 Gitlet – Mini Version Control System

Gitlet is a simplified version of Git implemented in Java.
It replicates core version control features like initializing a repository, staging files, committing snapshots, branching, and checkout.

---

📌 Features

- Initialize repository ("init")
- Stage files ("add")
- Commit snapshots ("commit")
- View commit history ("log")
- Create branches ("branch")
- Switch branches ("checkout")
- Basic merge functionality
- Persistent storage using ".gitlet" directory

---

🗂️ Project Structure

Gitlet/

├── src/
│   ├── Main.java          - Entry point (CLI handler)
│   ├── Repository.java    - Core logic (commands implementation)
│   ├── Commit.java        - Commit object structure
│   ├── Utils.java         - File handling utilities
│   └── AIService.java     - Optional AI integration

├── bin/                   - Compiled class files

├── .gitlet/               - Internal storage (auto-created)
│   ├── commits/           - Stores commit objects
│   ├── branches/          - Branch pointers
│   ├── staging/           - Staged files
│   └── HEAD               - Current branch reference

├── test.txt               - Sample file
└── README.md

---

⚙️ How to Run

1️⃣ Compile the project

javac -d bin src/*.java

2️⃣ Run commands

java -cp bin Main <command>

---

🧪 Commands Usage

🔹 Initialize repository

java -cp bin Main init

🔹 Add file to staging

java -cp bin Main add <filename>

🔹 Commit changes

java -cp bin Main commit "commit message"

🔹 View commit history

java -cp bin Main log

🔹 Create a new branch

java -cp bin Main branch <branch-name>

🔹 Switch branch

java -cp bin Main checkout <branch-name>

🔹 Merge branches (basic)

java -cp bin Main merge <branch-name>

---

🧠 How It Works (Architecture)

- "HEAD" → stores current branch name
- "branches/" → maps branch → latest commit ID
- "commits/" → stores serialized commit objects
- Each commit contains:
  - message
  - timestamp
  - parent commit reference
  - snapshot of files

---

⚠️ Limitations

- No remote repository support (push/pull)
- No advanced merge conflict resolution
- CLI-based only (no GUI)
- Limited error handling

---

💡 Future Improvements

- Add remote repository support
- Implement merge conflict resolution
- Add diff visualization
- Build a graphical UI

---

👨‍💻 Author

Akshay Sood

---

⭐ Note

This project is built for learning purposes to understand how Git works internally.
