#!/usr/bin/env bash

curl -XPUT 'localhost:9200/customer?pretty'

curl -XPUT 'localhost:9200/customer/external/1?pretty' -d '
{
  "name": "John Doe"
}'

curl -XGET 'localhost:9200/customer/external/1?pretty'

curl -XDELETE 'localhost:9200/customer?pretty'

curl 'localhost:9200/_cat/indices?v'

curl -XPUT 'localhost:9200/customer/external/1?pretty' -d '
{
  "name": "John Doe"
}'

curl -XPOST 'localhost:9200/customer/external?pretty' -d '
{
  "name": "Jane Doe"
}'

curl -XPOST 'localhost:9200/customer/external/2?pretty' -d '
{
  "name": "Jane Doe"
}'

curl -XPOST 'localhost:9200/customer/external/1/_update?pretty' -d '
{
  "doc": { "name": "Jane Doe" }
}'

curl -XPOST 'localhost:9200/customer/external/1/_update?pretty' -d '
{
  "doc": { "name": "Jane Doe", "age": 20 }
}'

curl -XDELETE 'localhost:9200/customer/external/2?pretty'

curl -XDELETE 'localhost:9200/customer/external/_query?pretty' -d '
{
  "query": { "match": { "name": "Jane" } }
}'

# adds two documents with index 1 and 2
curl -XPOST 'localhost:9200/customer/external/_bulk?pretty' -d '
{"index":{"_id":"1"}}
{"name": "John Doe" }
{"index":{"_id":"2"}}
{"name": "Jane Doe" }
'

# updates document with id 1 and deletes document with id 2
curl -XPOST 'localhost:9200/customer/external/_bulk?pretty' -d '
{"update":{"_id":"1"}}
{"doc": { "name": "John Doe becomes Jane Doe" } }
{"delete":{"_id":"2"}}
'

# loads from accounts.json
curl -XPOST 'localhost:9200/bank/account/_bulk?pretty' --data-binary @accounts.json
# prints the indexes information with head
curl 'localhost:9200/_cat/indices?v'