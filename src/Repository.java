import java.io.*;
import java.util.*;

public class Repository {
    static final String GITLET_DIR = ".gitlet";
    static final String COMMITS_DIR = GITLET_DIR + File.separator + "commits";
    static final String STAGING_DIR = GITLET_DIR + File.separator + "staging";
    static final String BRANCH_DIR = GITLET_DIR + File.separator + "branches";
    static final String HEAD_FILE = GITLET_DIR + File.separator + "HEAD";

    // INIT
    public static void init() {
        new File(GITLET_DIR).mkdirs();
        new File(COMMITS_DIR).mkdirs();
        new File(STAGING_DIR).mkdirs();
        new File(BRANCH_DIR).mkdirs();

        // default branch
        Utils.writeFile(BRANCH_DIR + File.separator + "main", "null");

        // HEAD stores branch name
        Utils.writeFile(HEAD_FILE, "main");

        System.out.println("Initialized empty Gitlet repository.");
    }

    // ADD
    public static void add(String fileName) {
        try {
            File file = new File(fileName);
            String content = Utils.readFile(file);

            File staged = new File(STAGING_DIR + File.separator + fileName);
            Utils.writeFile(staged.getPath(), content);

            System.out.println("File added to staging.");
        } catch (Exception e) {
            System.out.println("File not found.");
        }
    }

    // COMMIT
    public static void commit(String message) {
        try {
            File headFile = new File(HEAD_FILE);

            if (!headFile.exists()) {
                System.out.println("Repository not initialized.");
                return;
            }

            String currentBranch = Utils.readFile(headFile).trim();

            File branchFile = new File(BRANCH_DIR + File.separator + currentBranch);

            if (!branchFile.exists()) {
                System.out.println("Branch not found.");
                return;
            }

            String parent = Utils.readFile(branchFile).trim();
            if (parent.equals("null")) {
                parent = null;
            }

            File staging = new File(STAGING_DIR);
            File[] filesInStage = staging.listFiles();

            if (filesInStage == null || filesInStage.length == 0) {
                System.out.println("Nothing to commit.");
                return;
            }

            HashMap<String, String> files = new HashMap<>();

            for (File f : filesInStage) {
                files.put(f.getName(), Utils.readFile(f));
            }

            Commit commit = new Commit(message, parent, files);
            String commitId = UUID.randomUUID().toString();

            File commitFile = new File(COMMITS_DIR + File.separator + commitId);
            Utils.writeObject(commitFile, commit);

            // update branch pointer
            Utils.writeFile(branchFile.getPath(), commitId);

            System.out.println("Committed: " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOG (FIXED)
    public static void log() {
        try {
            // Step 1: get current branch
            String branch = Utils.readFile(new File(HEAD_FILE)).trim();

            // Step 2: get commit ID from branch file
            File branchFile = new File(BRANCH_DIR + File.separator + branch);

            if (!branchFile.exists()) {
                System.out.println("No commits yet.");
                return;
            }

            String head = Utils.readFile(branchFile).trim();

            if (head.equals("null")) {
                System.out.println("No commits yet.");
                return;
            }

            // Step 3: traverse commits
            while (head != null) {
                File commitFile = new File(COMMITS_DIR + File.separator + head);

                if (!commitFile.exists()) break;

                Commit commit = Utils.readObject(commitFile, Commit.class);

                if (commit == null) {
                    System.out.println("Error reading commit.");
                    break;
                }

                System.out.println("===");
                System.out.println("Commit ID: " + head);
                System.out.println("Message: " + commit.message);
                System.out.println("Time: " + commit.timestamp);
                System.out.println();

                head = commit.parentId;
            }

        } catch (Exception e) {
            System.out.println("Error reading log.");
        }
    }

    // BRANCH
    public static void branch(String name) throws IOException {
        File newBranch = new File(BRANCH_DIR + File.separator + name);

        if (newBranch.exists()) {
            System.out.println("Branch already exists.");
            return;
        }

        String currentBranch = Utils.readFile(new File(HEAD_FILE)).trim();
        String headCommit = Utils.readFile(new File(BRANCH_DIR + File.separator + currentBranch)).trim();

        Utils.writeFile(newBranch.getPath(), headCommit);

        System.out.println("Branch created: " + name);
    }

    // CHECKOUT
    public static void checkout(String branchName) {
        try {
            File branchFile = new File(BRANCH_DIR + File.separator + branchName);

            if (!branchFile.exists()) {
                System.out.println("Branch does not exist.");
                return;
            }

            String commitId = Utils.readFile(branchFile).trim();

            if (commitId.equals("null")) {
                System.out.println("No commits in this branch.");
                return;
            }

            File commitFile = new File(COMMITS_DIR + File.separator + commitId);
            Commit commit = Utils.readObject(commitFile, Commit.class);

            if (commit == null) {
                System.out.println("Error loading commit.");
                return;
            }

            // restore files
            for (String fileName : commit.files.keySet()) {
                Utils.writeFile(fileName, commit.files.get(fileName));
            }

            // update HEAD
            Utils.writeFile(HEAD_FILE, branchName);

            System.out.println("Switched to branch: " + branchName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MERGE (basic)
    public static void merge(String branchName) {
        try {
            String currentBranch = Utils.readFile(new File(HEAD_FILE)).trim();

            if (currentBranch.equals(branchName)) {
                System.out.println("Cannot merge same branch.");
                return;
            }

            File targetBranchFile = new File(BRANCH_DIR + File.separator + branchName);

            if (!targetBranchFile.exists()) {
                System.out.println("Branch does not exist.");
                return;
            }

            String currentCommitId = Utils.readFile(
                    new File(BRANCH_DIR + File.separator + currentBranch)).trim();

            String targetCommitId = Utils.readFile(targetBranchFile).trim();

            Commit currentCommit = Utils.readObject(
                    new File(COMMITS_DIR + File.separator + currentCommitId), Commit.class);

            Commit targetCommit = Utils.readObject(
                    new File(COMMITS_DIR + File.separator + targetCommitId), Commit.class);

            if (currentCommit == null || targetCommit == null) {
                System.out.println("Error loading commits.");
                return;
            }

            for (String fileName : targetCommit.files.keySet()) {
                String targetContent = targetCommit.files.get(fileName);
                String currentContent = currentCommit.files.get(fileName);

                if (currentContent == null) {
                    Utils.writeFile(fileName, targetContent);
                    System.out.println("Added file: " + fileName);
                } else if (!currentContent.equals(targetContent)) {
                    System.out.println("Conflict in file: " + fileName);
                }
            }

            System.out.println("Merge completed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // AI
    public static void ai(String query) {
        query = query.toLowerCase();
        String response = AIService.askGemini(query);

        response = response.replace("`", "").trim();

        if (!response.startsWith("gitlet")) {
            response = "gitlet " + response;
        }

        response = response.replace("u003c", "<")
                           .replace("u003e", ">");

        System.out.println("AI Response: " + response);
    }
}