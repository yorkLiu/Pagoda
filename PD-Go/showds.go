package main

import (
	"fmt"
	"os"
	"flag"
	"strings"
	"path"
)

/*Usage:

	go run showds.go
	go run showds.go -p ~/Downloads
	go run showds.go -p ~/Downloads -a 

*/

const (
	INDENT = "  "
	PATH_PREFIX = "|"
	FOLDER_PREFIX = "+ "
	FILE_PREFIX = "_ "
	
)

var (
	rootPath string
	appendPathLine bool
)

func init()  {
	flag.StringVar(&rootPath, "p", "", "The path of the target direcotry")
	flag.BoolVar(&appendPathLine, "a", false, "Append the path line '|' ")
}

func showFiles(basePath string, level string, filePrefix string, folderPrefix string,  showAll bool) error {
	base, err :=os.Open(basePath)
	if err != nil{
		return err
	}
	
	subs, err := base.Readdir(-1)
	if err != nil{
		return err
	}

	for _, v := range subs  {
		fi := v.(os.FileInfo)
		fn := fi.Name()
		if strings.HasPrefix(fn, ".") && !showAll {
			continue
		}
		
		if fi.IsDir() {
			subFp  := path.Join(basePath, fn)
			if err != nil {
				return err
			}
			fmt.Printf("%s\n", level + folderPrefix + subFp)
			
			err = showFiles(subFp, getLevel(level)+INDENT, filePrefix, folderPrefix , showAll)
			if err != nil {
				return err
			}
			
		} else {
			fmt.Printf("%s\n", getLevel(level) + filePrefix + fn)
		}
		
	}
	
	return nil
}

func getLevel(level string) string {
	if(appendPathLine){
		return level + PATH_PREFIX
	}
	return level
}

func main() {
	flag.Parse()
	if len(rootPath) == 0 {
		defaultPath, err := os.Getwd()
		if err != nil {
			fmt.Println("Getwd Error:", err)
			return 
		}
		rootPath = defaultPath
	}
	
	
	
	fmt.Printf("%s \n", FOLDER_PREFIX + rootPath)
	showFiles(rootPath, INDENT, FILE_PREFIX, FOLDER_PREFIX, false)
	
}
