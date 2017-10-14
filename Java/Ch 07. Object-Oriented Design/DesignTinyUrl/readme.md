
写文章

How tow design a TinyURL System
How tow design a TinyURL System
嘿咻
嘿咻
刚刚
Question Description

Over here

S: Scenario

Long URL to short URL and reversed.

N: Need (Assume the system is not massive if you are not sure)

QPS (queries per second)

Daily User: 100M
Daily usage per person: (Write) long2short 0.1, (Read) short2long 1
Daily request: Write 10M, Read 100M
QPS: Since a day is 86400s approximately 100K.
Write 100, Read 1K

Peak QPS: Write 200, Read 2K
(Thousand level can be handled by a single SSD MySQL Machine)

Storage

10M new mappings (long URL to short URL) per day
assume each mapping takes 100B in average
1GB every day. 1 TB hard drive could stand for 3 years.
Storage is not the problem for this kind of system. Service like Netflix may have storage issues.

Through SN analysis, we could have a big picture of the system. In general, this system is not hard and could be handled by a single SSD Machine.

A: API

Only one service: URLService

Core (Business Logic) Layer:
Class: URLService
Interface:
URLService.encode(String long_url)
URLService.decode(String short_url)
Web Layer:
REST API:
GET: /{short_url}, return a http redirect response(301)
POST: goo.gl method - google shorten URL
Request Body: {url=longUrl} e.g. {"longUrl": "http://www.google.com/"}
Return OK(200), short_url is included in the data

K: Data Access

Step 1: Pick a storage structure

SQL vs NoSQL?

Does it need to support transactions? NoSQL does not support transaction.
Do we need rich SQL query? NoSQL does not support as many queries as SQL.
Pursue development efficiency? Most Web Framework supports SQL database very well (with ORM). It means fewer codes for the system.
Do we need to use AUTO_INCREMENT ID? NoSQL couldn't do this. It only has a global unique Object_id.
Does the system has a high requirement for QPS? NoSQL has high performance. For example, Memcached's QPS could reach million level, MondoDB does 10K level, MySQL only supports K level.
How high is the system's scalability? SQL requires developers write their codes to scale, while NoSQL comes with them (sharding, replica).
Answer:

No -> NoSQL
No -> NoSQL
Doesn't matter because there are only a few codes. -> NoSQL
Our algorithm needs AUTO_INCREMENT ID. -> SQL
Write 200, Read 2K. Not high. -> SQL
Not high. -> SQL
System Algorithm

OK, let's talk about the system algorithm. There are following solutions:

Hash function:
long_url -> md5/sha1

md5 convert a string into 128 binary bits, generally represented as 16 bytes hex:
http://site.douban.com/chuan -> c93a360dc7f3eb093ab6e304db516653

sha1 convert a string into 160 binary bits, generally represented as 20 bytes hex:
http://site.douban.com/chuan -> dff85871a72c73c3eae09e39ffe97aea63047094

These two algorithms could make sure hash values are randomly distributed, but the conflicts are inevitable. Any hash algorithm could have inevitable conflicts.

Pros: Simple. We could take the first 6 chars of the converted string.
Cons: Conflicts.
Solutions: 1. use (long_url + timestamp) as the hash function key. 2. When conflicts, regenerates the hash value(it's different because timestamp changes).

Overall, when urls are over 1 billion, there would be a lot of conflicts and the efficiency could be very low.

base62
Take short_url as a 62 base notation. 6 bits could represent 62^6=57 billion.
Each short_url represent a decimal digit. It could be the auto_increment_id in SQL database.

Step 2: Database Schema

One table (id, long_url). id is the primary key, ordered by long_url

The basic system architecture:

Browser <-> Web <-> Core <-> DB

O: optimize

How to improve the response speed?

Improve the response speed between web server and database

Use Memcached to improve response speed. When getting long_url, search in the cache first, then database. We could put 90% read request on the cache.

Improve the response speed between web server and user's browser

Different locations use different web server and cache server. All the areas share a DB used to match the users to the closest web server (through DNS) when they have a miss on the cache.

What if we need one more MySQL machine?

Issues:

running out of cache
More and more write requests
More and more cache misses
Solutions:

Database Partitioning

Vertical sharding 2. Horizontal sharding
The best way is horizontal sharding.

Currently table structure is (id, long_url). So, which column should be sharding key?

An easy way is id modulo sharding.

Here comes another question: How could multiple machines share a global auto_increment_id?

Two ways: 1. use one more machine to maintain the id. 2. use zookeeper. Both suck.

So, we do not use global auto_increment_id.

The pro way is put the sharding key as the first byte of the short_url.

Another way is to use consistent hashing to break the cycle into 62 pieces. It doesn't matter how many pieces because there probably would not be over 62 machines (it could be 360 or whatever). Each machine is responsible for the service in the part of the cycle.

write long_url -> hash(long_url)%62 -> put long_url to the specific machine according to hash value -> generate short_url on this machine -> return short_url

short_url request -> get the sharding key (first byte of the short_url) -> search in the corresponding machine based on sharding key -> return long_url

Each time we add a new machine, put half of the range of the most used machine to the new machine.

More Optimization

Put Chinese DB in China, American DB in the United States. Use geographical information as the sharding key, e.g. 0 for Chinese websites, 1 for American websites.

Java Web
面试
Java 入门
0
收藏
分享
编辑

还没有评论

嘿咻
写下你的评论...


选择语言
