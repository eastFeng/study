--获取KEY
local key1 = KEYS[1]

local val = redis.call('incr', key1)
local ttl = redis.call('ttl', key1)

--获取ARGV内的参数
local expire = ARGV[1]
local times = ARGV[2]

if val == 1 then
    redis.call('expire', key1, tonumber(expire))
else
    if ttl == -1 then
        redis.call('expire', key1, tonumber(expire))
    end
end

if val > tonumber(times) then
    return 0
end

return 1