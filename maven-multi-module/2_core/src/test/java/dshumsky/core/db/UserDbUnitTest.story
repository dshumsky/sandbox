GivenStories: dshumsky/core/db/T02_PERMISSION_TYPE.story
Lifecycle:
Before:
Given DB: Table T01_USER:
| C01_USER_ID | C01_USERNAME | C01_PASSWORD                                                 | C01_FIRSTNAME | C01_LASTNAME | C01_EMAIL          | C01_ENABLED |
| 1           | admin        | $2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi | Admin         | Admin        | admin@tp.com       | 1           |
| 2           | usermanager  | $2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO | UserManager   | UserManager  | usermanager@tp.com | 1           |
| 3           | user1        | $2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO | User1         | User1        | user1@tp.com       | 1           |
| 4           | user2        | $2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO | User2         | User2        | user2@tp.com       | 1           |
| 5           | disabled     | $2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO | Disabled      | Disabled     | disabled@tp.com    | 1           |
Given DB: setUp
After:
Given DB: tearDown

Scenario: Scenario 1
 
Then test
