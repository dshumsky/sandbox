Scenario: Closest greater element for every array element from another array
two arrays a[] and b[], we need to build an array c[] such that every element c[i] of c[]
contains a value from a[] which is greater than b[i] and is closest to b[i].
If a[] has no greater element than b[i], then value of c[i] is -1.
All arrays are of same size.

When a[]= 2,6,5,7,0 and b[]= 1,3,2,5,8
Then с[]= 2,5,5,6,-1

When a[]= 2,6,5,7,0 and b[]= 0,2,3,5,1
Then с[]= 2,5,5,6,2
