# 🚀 Gitlet – Mini Version Control System

## 📖 Description

Gitlet is a simplified version of Git implemented in Java.
It provides core version control features like staging files, committing snapshots, branching, and switching between versions.

---

## 📌 Features

| Command    | Description                 |
| ---------- | --------------------------- |
| `init`     | Initialize repository       |
| `add`      | Stage files                 |
| `commit`   | Create snapshot             |
| `log`      | Show commit history         |
| `branch`   | Create new branch           |
| `checkout` | Switch branch               |
| `merge`    | Merge branches              |
| `ai`       | AI-based command suggestion |

---


## ⚙️ Commands to Run

### Compile

```bash
javac -d bin src/*.java
```

### Run Commands

```bash
java -cp bin Main init
java -cp bin Main add <file>
java -cp bin Main commit "message"
java -cp bin Main log
java -cp bin Main branch <name>
java -cp bin Main checkout <name>
java -cp bin Main merge <name>
```

---

## 🧠 High-Level Design (HLD)

👉 [View Architecture Diagram](./HLD.png)

---

## 🧠 Low-Level Design (LLD)

👉 [View Architecture Diagram](./LLD.png)

---

## 👨‍💻 Author

Akshay Sood
