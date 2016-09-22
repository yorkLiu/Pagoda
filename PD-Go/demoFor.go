
package main
import (
	"fmt"
)

func main() {
	
	var a int
	var b int = 15
	
	for i:=0;i<10;i++{
		fmt.Printf("index: %d\n", i)
	}
	
	for a<b {
		a++
		fmt.Printf("a,b: %d, %d\n", a, b)
	}
	
	// iterator a array
	array := []string {"A", "B", "C", "D", "E"}
	for i,x:=range  array {
		fmt.Printf("value of x = %s at %d\n", x,i)
	}
	fmt.Printf("%s len=%d, cap=%d\n", array, len(array), cap(array))
	
	// find the prime number from 1~100
	var i, j int
	for i=2;i<100;i++{
		isPrime:= true
		for j=2;j<i;j++{
			if i %j == 0 {
				isPrime = false
				break
			}
		}
		if(isPrime){
			fmt.Printf("%d is Prime\n", i)	
		}
	}
}
