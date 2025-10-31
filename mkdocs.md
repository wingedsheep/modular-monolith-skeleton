# Documentation Setup Guide

## Overview

The NMA project uses [MkDocs](https://www.mkdocs.org/) with the [Material theme](https://squidfunk.github.io/mkdocs-material/) for generating project documentation. The documentation is located in the `documentation/` directory and includes architectural decision records (ADRs), technical specifications, and development guides.

## Prerequisites

Choose one of the following setups:

### Option 1: Docker (Recommended)
- [Docker](https://www.docker.com/) or [Podman](https://podman.io/)
- [just](https://github.com/casey/just) command runner

### Option 2: Local Python
- Python 3.11 or higher
- [Poetry](https://python-poetry.org/) for dependency management

## Quick Start

### Using Docker (Easiest)

Navigate to the documentation directory and start the services:

```bash
cd documentation
just docker-up
```

Access the documentation at: http://localhost:8000

The Docker setup includes:
- **MkDocs server** - Serves the documentation with live reload
- **Kroki services** - Renders diagrams (PlantUML, Mermaid, BPMN, Excalidraw)

### Using Poetry (Without Docker)

Install dependencies and serve:

```bash
cd documentation
poetry install
poetry run mkdocs serve
```

⚠️ **Note**: Diagram rendering (Kroki) won't work without Docker services running separately.

## Key Features

### Plugins & Extensions

The documentation setup includes:

- **mkdocs-material** - Modern, responsive theme
- **mkdocs-kroki-plugin** - Diagram rendering (PlantUML, Mermaid, BPMN)
- **mkdocs-glightbox** - Image lightbox functionality
- **mkdocs-table-reader-plugin** - Import tables from CSV/Excel
- **mkdocs-swagger-ui-tag** - OpenAPI/Swagger documentation
- **mkdocs-awesome-nav** - Enhanced navigation customization

### Diagram Support

Create diagrams directly in markdown using Kroki:

````markdown
```kroki-plantuml
@startuml
actor User
User -> System: Request
System -> Database: Query
@enduml
```
````

Supported formats: PlantUML, Mermaid, BPMN, Excalidraw

## Available Commands (using just)

```bash
# Start documentation server
just docker-up

# Stop all services
just docker-down

# Build documentation site
just poetry-build

# Install/update dependencies
just poetry-install
just poetry-update

# Run Structurizr (for architecture diagrams)
just docker-structurizr-up
```

## Project Structure

```
documentation/
├── docs/                           # Documentation content
│   └── software-architecture/
│       └── architectural-decision-records/
├── docker/                         # Docker configurations
│   ├── docker-compose.yml
│   └── Dockerfile.serve
├── mkdocs.yml                      # MkDocs configuration
├── pyproject.toml                  # Poetry dependencies
├── justfile                        # Command shortcuts
└── README.md
```

## Writing Documentation

### Creating ADRs

Architectural Decision Records follow this structure:

```markdown
# ADR-XX: Title

| **Status**       | IN PROGRESS / ACCEPTED / REJECTED |
|------------------|-----------------------------------|
| **Impact**       | LOW / MEDIUM / HIGH               |
| **Driver**       | Name                              |

## 📘 Background
Context and problem statement

## 📚 Relevant data
Supporting information

## 🌈 Options considered
Available alternatives

## ✅ Action items
Implementation steps

## 🌟 Outcome
Final decision and impact
```

### Adding Pages

1. Create markdown files in `docs/`
2. Update navigation in `mkdocs.yml` (or use auto-discovery)
3. Use relative links: `[Link text](../path/to/page.md)`

## Maintenance

### Updating Dependencies

Keep dependencies current:

```bash
poetry update
```

This updates all packages to their latest compatible versions as specified in `pyproject.toml`.

### Building for Production

Generate static site:

```bash
poetry run mkdocs build
```

Output is in `site/` directory, ready for deployment.

## Troubleshooting

**Diagrams not rendering locally?**
- Ensure Docker services are running (`just docker-up`)
- Check Kroki is accessible at http://localhost:8000

**Port 8000 already in use?**
- Stop other services using port 8000
- Or modify the port in `docker-compose.yml`

**Poetry dependency conflicts?**
- Delete `poetry.lock` and run `poetry install` again
- Check Python version compatibility (3.11+)

## Additional Resources

- [MkDocs Documentation](https://www.mkdocs.org/)
- [Material Theme Guide](https://squidfunk.github.io/mkdocs-material/)
- [Kroki Documentation](https://docs.kroki.io/)
- [Poetry Documentation](https://python-poetry.org/docs/)
