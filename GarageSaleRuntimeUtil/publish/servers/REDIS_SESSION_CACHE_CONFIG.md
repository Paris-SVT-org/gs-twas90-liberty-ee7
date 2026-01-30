# Redis Session Cache Configuration

## Overview
The Liberty server has been configured to use Redis cluster for HTTP session caching instead of database-backed sessions. This provides better performance and scalability for session management.

## Configuration Changes

### 1. Feature Manager
- **Removed**: `sessionDatabase-1.0`
- **Added**: `sessionCache-1.0`

### 2. Session Storage
- **Removed**: Database-backed session storage (`httpSessionDatabase` and `SessionDS` dataSource)
- **Added**: Redis-backed session cache (`httpSessionCache`)

## Required Environment Variables

You must set the following environment variables for the Redis cluster connection:

### REDIS_URI
The Redis connection URI. For a Redis cluster, use the format:
```
redis://host1:port1,host2:port2,host3:port3
```

Example:
```
REDIS_URI=redis://redis-node1:6379,redis-node2:6379,redis-node3:6379
```

For Redis Sentinel:
```
REDIS_URI=redis-sentinel://sentinel1:26379,sentinel2:26379?sentinelMasterId=mymaster
```

### REDIS_SSL_ENABLED
Enable SSL/TLS for Redis connections:
```
REDIS_SSL_ENABLED=true
```
or
```
REDIS_SSL_ENABLED=false
```

### REDIS_PASSWORD
The password for Redis authentication (if required):
```
REDIS_PASSWORD=your_redis_password
```

## Setting Environment Variables

### Option 1: server.env file
Add to `GarageSaleRuntimeUtil/publish/files/server.env`:
```properties
REDIS_URI=redis://redis-node1:6379,redis-node2:6379,redis-node3:6379
REDIS_SSL_ENABLED=true
REDIS_PASSWORD=your_redis_password
```

### Option 2: System Environment Variables
Set as system environment variables before starting the Liberty server.

### Option 3: Docker/Kubernetes
Pass as environment variables in your container configuration:
```yaml
env:
  - name: REDIS_URI
    value: "redis://redis-node1:6379,redis-node2:6379,redis-node3:6379"
  - name: REDIS_SSL_ENABLED
    value: "true"
  - name: REDIS_PASSWORD
    valueFrom:
      secretKeyRef:
        name: redis-secret
        key: password
```

## Redis Client Library

The configuration expects Redis client JAR files in:
```
${shared.resource.dir}/redis/
```

### Required JARs
You need to add Redis client libraries. Recommended options:

#### Option 1: Jedis (Recommended)
- jedis-x.x.x.jar
- commons-pool2-x.x.x.jar

#### Option 2: Lettuce
- lettuce-core-x.x.x.jar
- netty-all-x.x.x.jar
- reactive-streams-x.x.x.jar

### Installation Steps
1. Create the directory:
   ```bash
   mkdir -p GarageSaleRuntimeUtil/publish/redis
   ```

2. Download and copy the Redis client JARs to this directory

3. Restart the Liberty server

## Session Configuration Details

The `httpSession` element is configured with:
- `cloneId="localhost_cloneID"`: Identifies this server instance
- `storageRef="httpSessionCache"`: References the Redis cache for session storage

## Benefits of Redis Session Cache

1. **Performance**: In-memory storage provides faster session access
2. **Scalability**: Easy horizontal scaling with Redis cluster
3. **High Availability**: Redis cluster provides automatic failover
4. **Session Sharing**: Multiple Liberty instances can share sessions
5. **Reduced Database Load**: Removes session storage burden from database

## Troubleshooting

### Connection Issues
Check Liberty logs for Redis connection errors:
```
${server.output.dir}/logs/messages.log
```

### Verify Environment Variables
Ensure all required environment variables are set:
```bash
echo $REDIS_URI
echo $REDIS_SSL_ENABLED
echo $REDIS_PASSWORD
```

### Test Redis Connection
Use Redis CLI to verify cluster connectivity:
```bash
redis-cli -c -h redis-node1 -p 6379 -a your_password ping
```

## Migration Notes

### From Database Sessions
- Old database sessions will not be automatically migrated
- Users will need to re-authenticate after the switch
- Consider a maintenance window for the migration

### Rollback
To rollback to database sessions:
1. Change `sessionCache-1.0` back to `sessionDatabase-1.0`
2. Restore the `httpSessionDatabase` and `SessionDS` configuration
3. Restart the server

## Additional Resources

- [Liberty sessionCache-1.0 Documentation](https://www.ibm.com/docs/en/was-liberty/base?topic=liberty-sessioncache-10)
- [Redis Cluster Tutorial](https://redis.io/topics/cluster-tutorial)
- [Redis Sentinel Documentation](https://redis.io/topics/sentinel)