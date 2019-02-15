Scenario:
There is a text file in hadoop.
Each line contains temperature record in format "<Long timestamp>,<Double value>"
The scenario prepares file, aggregates values by time and calculates 'average' and 'count'.

When HDFS: append text to /dir1/temperature.txt (overwrite=true)
| Content |
| 12,24   |
| 12,26   |
| 13,10   |
| 13,11   |
| 13,12   |

Then aggregationByTime for /dir1/temperature.txt is
|    | averageTemperature | count |
| 12 | 25                 | 2     |
| 13 | 11                 | 3     |
