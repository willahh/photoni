Scenario: create a new product
# this is a comment
When I create a new product with name 'iphone 6' and description 'awesome phone'
Then I receive a response with an id 56422 and a location URL
# this a second comment
# on two lines
When I invoke a GET request on location URL
Then I receive a 200 response

Scenario: get product info
When I invoke a GET request on location URL
Then I receive a 200 response