# GitHub Repository Setup

## CI/CD via GitHub Actions

The workflow at `.github/workflows/ci.yml` runs automatically on every push to
`master`. It has two jobs that run in sequence:

| Job | Runner | What it does |
|-----|--------|--------------|
| **Build & Test** | `ubuntu-latest` (JVM) | Compiles with Maven, runs the full test suite and packages the application as a JAR file. |
| **Deploy** | `ubuntu-latest` | Copies the JAR to the server via SCP and restarts the application. |

## Required secrets

Go to **Settings → Secrets and variables → Actions → New repository secret** and
add the following:

| Secret | Description |
|--------|-------------|
| `DEPLOY_HOST` | IP address or hostname of the target server |
| `DEPLOY_USER` | SSH username used for the connection |
| `DEPLOY_SSH_KEY` | Private SSH key (PEM format) whose public key is in `~/.ssh/authorized_keys` on the server |

`GITHUB_TOKEN` is provided automatically by GitHub — no manual configuration needed.

### Generating an SSH key pair for deployment

```bash
ssh-keygen -t ed25519 -C "github-actions-deploy" -f deploy_key -N ""
```

- Add the content of `deploy_key.pub` to `~/.ssh/authorized_keys` on the server
- Add the content of `deploy_key` as the `DEPLOY_SSH_KEY` secret in GitHub
- Delete both local files afterwards

## Server prerequisites

All commands below are run on the server. Replace `deploy` with your actual
`DEPLOY_USER` wherever it appears.

### 1. Create the deploy user (if it does not exist yet)

```bash
sudo useradd --system --create-home --shell /bin/bash deploy
```

### 2. Create the application directory

```bash
sudo mkdir -p /opt/coraquest
sudo chown deploy:deploy /opt/coraquest
```

The deploy job will write the JAR here. The data directory for the
file-based database (`/opt/coraquest/data/`) is created automatically by the
application on first start.

### 3. Authorise the deploy SSH key

Switch to the deploy user and add the public key generated earlier:

```bash
sudo -u deploy bash
mkdir -p ~/.ssh
chmod 700 ~/.ssh
echo "<paste content of deploy_key.pub here>" >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
exit
```

Test from your local machine before proceeding:

```bash
ssh -i deploy_key deploy@<DEPLOY_HOST> "echo OK"
```

### 4. Create run script

Create `/opt/run-cora.sh` as root:

```bash
sudo tee /opt/run-cora.sh > /dev/null << 'EOF'
#!/bin/bash
java -jar /opt/coraquest-progress-to-bgg-0.0.1.jar
EOF
```

Make it executable:
```bash
sudo chmod +x /opt/run-cora.sh
```

## Dependabot

The file `.github/dependabot.yml` is already committed to the repository. It
instructs Dependabot to check for outdated dependencies every Monday and open
pull requests automatically:

| Ecosystem | What is checked |
|-----------|-----------------|
| `maven` | All dependencies and plugins in `pom.xml` |
| `github-actions` | Action versions used in `.github/workflows/ci.yml` |

### Enable Dependabot in the repository

Dependabot reads the config file automatically, but the feature may need to be
switched on once per repository:

1. Go to **Settings → Security → Code security and analysis**
2. Enable **Dependabot version updates**
3. Optionally also enable **Dependabot security updates** — this opens PRs for
   known CVEs independently of the weekly schedule

After enabling, GitHub will scan the repository and open the first batch of PRs
within a few minutes.

### Handling Dependabot PRs

Dependabot PRs run the full CI pipeline. Because the deploy job runs on every
push to `master`, **do not merge Dependabot PRs directly into `master`** without
reviewing the changes — a merged PR triggers a deploy to the production server.

If branch protection is configured (see below), the `Build & Test` check must
pass before a Dependabot PR can be merged, which is sufficient protection for
dependency bumps.

## Recommended branch protection (optional)

Go to **Settings → Branches → Add branch protection rule** for `master` and enable:

- **Require status checks to pass before merging**
  - Add `Build & Test` as a required check
- **Require branches to be up to date before merging**

The deploy job is not required to block pull request
merges.