package main

import "fmt"

func main() {
	
	var i int = 5
	if i+=4; i < 10 {
		fmt.Print("i:", i)
	} else if i > 10 {
		fmt.Printf("I: ", i)
	}
	
}
