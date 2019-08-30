When HDFS: clean folder /words/files
When HDFS: clean folder /words/toProcess
When HDFS: clean folder /words/checkpointDirectory

When HDFS: append text to /words/files/input1.txt (overwrite=true)
| Content           |
| word1 word2 word3 |
| word2             |
| word3 word3       |

When HDFS: append text to /words/files/input2.txt (overwrite=true)
| Content           |
| word5 word5 word5 |
| word5 word4 word4 |
| word5 word4 word4 |

When start counting

Then checkStatus
|       | <this> |

When HDFS: /words/files/input1.txt copyTo /words/toProcess/input1.txt
When sleep 5000 ms
Then checkStatus
|       | <this> |
| word1 | 1      |
| word2 | 2      |
| word3 | 3      |

When HDFS: /words/files/input2.txt copyTo /words/toProcess/input2.txt
When sleep 5000 ms
Then checkStatus
|       | <this> |
| word1 | 1      |
| word2 | 2      |
| word3 | 3      |
| word4 | 4      |
| word5 | 5      |

When HDFS: /words/files/input1.txt copyTo /words/toProcess/input1-2.txt
When sleep 5000 ms
Then checkStatus
|       | <this> |
| word1 | 2      |
| word2 | 4      |
| word3 | 6      |
| word4 | 4      |
| word5 | 5      |

Then stop