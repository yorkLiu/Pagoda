package main
import "fmt"

func main() {
	
	var countryMap map[string]string
	countryMap = make(map[string]string)
	
	// new static map
	provinceMap := map[string]string{"BJ": "Bei Jing", "TJ": "Tian Jin", "SH": "Shang Hai" }
	printMap2(provinceMap)
	
	countryMap["France"] = "Paris"
	countryMap["Janpan"] = "Tokyo"
	countryMap["India"] = "New Delphi"
	
	printMap(countryMap)
	
	// delete countryMap 
	delete(countryMap, "Janpan")
	fmt.Println("-------delete 'Janpan' from countryMap-------")
	printMap2(countryMap)
}

func printMap(tmap map[string]string){
	for key:=range tmap{
		fmt.Printf("City: %s, Captial: %s\n", key, tmap[key])
	}
}

func printMap2(tmap map[string]string){
	for key, value:=range tmap{
		fmt.Printf("City: %s, Captial: %s\n", key, value)
	}
}
