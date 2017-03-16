package main

import "fmt"

func f(i int) int  {
	fmt.Println(i)
	return i *10
}

func fibonacci(num int) int {
	if num == 0 {
		return 0
	}
	if num < 2 {
		return 1
	}
	return fibonacci(num-1) + fibonacci(num-2)
}


func main() {
	//defer fmt.Println("...start....")
	//for i:=1; i<5; i++  {
	//	defer fmt.Println(f(i))
	//}
	//defer fmt.Println("...Done....")

	for i := 0; i < 10; i++ {
		num := fibonacci(i)
		fmt.Printf("%d ", num)
		defer fmt.Printf("%d ", num)
	}
	
	
}
