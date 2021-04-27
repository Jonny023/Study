@set "now=%DATE:~0,4%_%DATE:~5,2%_%DATE:~8,2%_%TIME:~0,2%_%TIME:~3,2%_%TIME:~6,2%"
copy *.sql merge_%now%.sql