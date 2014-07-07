Integer-Factorization
=====================

John Paul Smith

A simple stand-alone program to demonstrate the basics of prime decomposition:
input a positive integer and receive that integer's prime constituents. This 
general problem is a key component of public key cryptography. 

I originally create this program to solve the third Project Euler challenge 
problem: http://projecteuler.net/problem=3.

The algorithm to determine the prime factors is a tuned trial division
implementation, with a maximum input value equal to the maximum positive
value of a 64-bit integer in the JVM. 
 
In expectation, this program probably won't take much longer than 90 seconds 
to factor the input number, because most numbers are comprised of several to
many smaller prime factors. However, a number that is the square of a 
ten-digit prime or the product of two ten-digit primes is the worst-case for 
this program and the general problem of integer factorization as a whole. To
see this, choose 9223371873002223329 as the input. As the only factors are 
3037000493 and 3037000453, the algorithm will take a very long time to find 
them, likely between six and eight hours on a relatively fast personal 
computer. 

Recommended test inputs: 7653567865434567, 35184372088832, and 600851475143.