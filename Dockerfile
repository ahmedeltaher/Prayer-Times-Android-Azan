FROM python:3.12-slim
WORKDIR /app
RUN pip install --no-cache-dir uv
COPY pyproject.toml README.md ./
COPY src/ ./src/
RUN uv pip install --system --no-cache tzdata .
ENV PYTHONUNBUFFERED=1
CMD ["python", "-m", "azan_mcp.server"]