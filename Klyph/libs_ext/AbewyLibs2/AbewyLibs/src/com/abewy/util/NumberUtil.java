/**
 * @author Jonathan
 */

package com.abewy.util;

import java.util.Arrays;

public class NumberUtil
{

	/**
	 * Determine if a number is prime or not
	 * Prime numbers : numbers which can be divided only by 1 or themselves
	 */
	public static boolean isPrime(int n)
	{
		if (n <= 1)
			return false;

		if (n == 2)
			return true;

		if (n % 2 == 0)
			return false;

		int m = (int) Math.sqrt(n);

		for (int i = 3; i <= m; i += 2)
		{
			if (n % i == 0)
				return false;
		}

		return true;
	}

	/**
	 * Get the first prime numbers until index n
	 */
	public static boolean[] sieve(int n)
	{
		boolean[] prime = new boolean[n + 1];
		Arrays.fill(prime, true);
		prime[0] = false;
		prime[1] = false;
		
		int m = (int) Math.sqrt(n);

		for (int i = 2; i <= m; i++)
		{
			if (prime[i])
			{
				for (int k = i * i; k <= n; k += i)
				{
					prime[k] = false;
				}
			}
		}
		
		return prime;
	}

	/*
	 * Return the greatest common divisor of two numbers
	 * a and b must not be 0
	 */
	public static int GCD(int a, int b)
	{
		if (b == 0)
			return a;

		return GCD(b, a % b);
	}

	/*
	 * Return the lowest common multiple of two numbers
	 * a and b must not be 0
	 */
	public static int LCM(int a, int b)
	{
		return b * a / GCD(a, b);
	}
}
