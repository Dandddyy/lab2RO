package main

import (
	"fmt"
	"math"
	"math/rand"
	"time"
)
func holiday(){
	const size = 16

	energy := createMonks(size)

	rounds := int(math.Log2(float64(size)))
	fights := size / 2

	for i := 1; i <= rounds; i++ {
		nextMonk := make(chan int, fights)
		if i == rounds {
			go fight(energy, nextMonk)
			winner := <-nextMonk
			fmt.Printf("\nWinner of the fight is: %d\n", winner)
			break
		}

		for i := 0; i < fights; i++ {
			go fight(energy, nextMonk)
		}

		fights /= 2
		energy = nextMonk
	}
}

func createMonks(size int) chan int {
	energy := make(chan int, size)

	s := rand.NewSource(time.Now().Unix())
	random := rand.New(s)

	for i := 0; i < size; i++ {
		val := random.Intn(150) + 1
		fmt.Print(val, " ")
		energy <- val
	}
	fmt.Println()
	return energy
}

func fight(energyRead chan int, energyWrite chan int) {
	rival1 := <-energyRead
	rival2 := <-energyRead
	if rival1 > rival2 {
		fmt.Printf("Fight %d vs %d : %d wins\n", rival1, rival2, rival1)
		energyWrite <- rival1
	} else {
		fmt.Printf("Fight %d vs %d : %d wins\n", rival1, rival2, rival2)
		energyWrite <- rival2
	}
}

func main() {
	holiday();
}
