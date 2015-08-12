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

# searches all the documents
curl 'localhost:9200/bank/_search?q=*&pretty'
# the same
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match_all": {} }
}'
# only the first one
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match_all": {} },
  "size": 1
}'
# 11th to 20th document
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match_all": {} },
  "from": 10,
  "size": 10
}'
# ordered by balance field
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match_all": {} },
  "sort": { "balance": { "order": "desc" } }
}'
# only account_number and balance fields
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match_all": {} },
  "_source": ["account_number", "balance"]
}'
# account with number 20
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match": { "account_number": 20 } }
}'
# contains 'mill' in the address
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match": { "address": "mill" } }
}'
# contains 'mill' or 'lane' in the address
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match": { "address": "mill lane" } }
}'
# the same but with bool query. 'should' means at least one of the clauses is true
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": {
    "bool": {
      "should": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}'
# contains 'mill lane' in the address
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": { "match_phrase": { "address": "mill lane" } }
}'
# the same but with bool query. 'must' means all the clauses are true
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": {
    "bool": {
      "must": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}'
# 'must_not' means all the clauses should be false
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": {
    "bool": {
      "must_not": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}'
# must, must_not and should can be combined as 'and'
curl -XPOST 'localhost:9200/bank/_search?pretty' -d '
{
  "query": {
    "bool": {
      "must": [
        { "match": { "age": "40" } }
      ],
      "must_not": [
        { "match": { "state": "ID" } }
      ]
    }
  }
}'