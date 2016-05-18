Statistics with Spark 1.6.1
---------------------------

Running the app
===============

- Add the spark-submit executable to your PATH
- In the root of this project, execute "./run"
- This is a pretty "noisy" way to see the results, so I've included a couple of REPL files in case that is easier to see the output.


ML - REPL Version
============

- This is a script / REPL version you can copy and paste.
- Make sure data.txt is in the correct folder.
- This is in src-script/stats.ml.scala


DataFrames - REPL Version
============

- This is a script / REPL version you can copy and paste.
- Make sure data.txt is in the correct folder.
- This is in src-script/stats.dataframes.scala
- start your spark shell with the databricks CSV library
   - ./spark-shell --packages com.databricks:spark-csv_2.11:1.4.0

