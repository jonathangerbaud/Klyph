package com.abewy.android.extended.util;

import java.math.BigInteger;
import java.util.ArrayList;

public class MathUtil
{

	/**
	 * Return the greatest common divisor of two long values
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static long gcd(long m, long n)
	{
		while (n != 0)
		{
			long rem = m % n;
			m = n;
			n = rem;
		}
		return m;
	}

	/**
	 * Return the greatest common divisor of two int values
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static int gcd(int m, int n)
	{
		while (n != 0)
		{
			int rem = m % n;
			m = n;
			n = rem;
		}
		return m;
	}

	//___ Factorial ___
	/**
	 * Compute the factorial of a value
	 * Use this method for small values. For larger values, use <code>factorial2</code>
	 * 
	 * @param n must be >= 0
	 * @return the factorial value
	 * @see factorial2
	 */
	public static long factorial(int n)
	{
		if (n <= 1)
			return 1;
		else
			return n * factorial(n - 1);
	}
	
	private static ArrayList<Long>	factorialCache	= new ArrayList<Long>();
	static
	{
		factorialCache.add((long) 1); // 0! = 1
		factorialCache.add((long) 1); // 1! = 1
	}
	
	/**
	 * Compute the factorial of a value
	 * Use this method for large values. For small values, you can use <code>factorial</code>
	 * 
	 * @param n must be >= 0
	 * @return the factorial value
	 * @see factorial
	 */
	public static long factorial2(int n)
	{
		if (n >= factorialCache.size())
		{
			factorialCache.add(n, n * factorial(n - 1));
		}
		
		return factorialCache.get(n);
	}
	//___ === ___
	

	// ___ Fibonacci ___

	private static ArrayList<BigInteger>	fibCache	= new ArrayList<BigInteger>();
	static
	{
		fibCache.add(BigInteger.ZERO);
		fibCache.add(BigInteger.ONE);
	}

	public static BigInteger fibonacci(int n)
	{
		if (n >= fibCache.size())
		{
			fibCache.add(n, fibonacci(n - 1).add(fibonacci(n - 2)));
		}
		
		return fibCache.get(n);
	}

	// ___ === ___
}
