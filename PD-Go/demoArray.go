package main
import "fmt"

func main() {
	
	var array = []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
	printSlice(array)
	
	fmt.Println("array[1:4]", array[1:4])
	fmt.Println("array[:3]", array[:3])
	fmt.Println("array[4:]", array[4:])
	fmt.Println("array[1:3:5]", array[1:3:3])
	
	array1 := make([]int, 0, 5)
	printSlice(array1)
	
	// append 
	array1 = append(array1, 21)
	array1 = append(array1, 22, 34)
	printSlice(array1)
	
	array2 := make([]int, len(array1))
	copy(array2, array1)
	printSlice(array2)
	
	for i:=range array1{
		fmt.Printf("%d\n", &array1[i]);
	}

	for i:=range array2{
		fmt.Printf("%d\n", &array2[i]);
	}
}

func printSlice(x []int){
	fmt.Printf("len=%d cap=%d slice=%v\n",len(x),cap(x),x)
}
