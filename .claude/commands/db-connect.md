# db-connect

Connects to the Vegas PostgreSQL database using credentials from `.env` and runs SQL.

`psql` not installed on this machine — use `psycopg2` (Python, available).

## Run SQL

```bash
source <(grep -v '^#' /home/santiago/vegas/vegas-backend/.env | sed 's/;$//' | sed 's/^/export /') && python3 -c "
import psycopg2, os
url = os.environ['DB_URL'].replace('jdbc:postgresql://', '')
host, rest = url.split(':', 1)
port, dbname = rest.split('/', 1)
conn = psycopg2.connect(host=host, port=int(port), dbname=dbname, user=os.environ['DB_USERNAME'], password=os.environ['DB_PASSWORD'])
conn.autocommit = True
cur = conn.cursor()
cur.execute('<SQL_QUERY>')
# For SELECT: print(cur.fetchall())
conn.close()
"
```

## Connection details (from .env)

- Host: `las-vegas-db.c5u8kgg2c5oy.us-east-2.rds.amazonaws.com`
- Port: `5432`
- DB: `postgres`
- User: `postgres`
