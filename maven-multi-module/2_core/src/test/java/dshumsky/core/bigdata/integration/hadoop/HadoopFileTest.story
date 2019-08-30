Scenario: scenario description

When HDFS: append text to /dir1/file1.txt (overwrite=true)
| Content |
| line1   |
| line2   |

Then HDFS: content of file /dir1/file1.txt is
| Content |
| line1   |
| line2   |

When HDFS: append text to /dir1/file1.txt (overwrite=false)
| Content |
| line3   |

Then HDFS: content of file /dir1/file1.txt is
| Content |
| line1   |
| line2   |
| line3   |


