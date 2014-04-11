/**
 * @author Jonathan
 */

package com.abewy.android.extended.util;

import java.util.List;

public class SearchUtil
{
	public static final int	NOT_FOUND	= -1;

	/**
	 * <p>Search an object in an array of object using a binary search.</p>
	 * <p>Binary search is O(log N) complexity</p>
	 * <p>The array must be sorted from lower to higher</p>
	 * 
	 * @param a the array to search in
	 * @param x the object to search for
	 * @return the index of the object, -1 if not found
	 */
	public static <T extends Comparable<? super T>> int binarySearch(T[] a, T x)
	{
		int low = 0, high = a.length - 1;

		while (low <= high)
		{
			int mid = (low + high) / 2;

			int result = a[mid].compareTo(x);

			if (result < 0)
				low = mid + 1;
			else if (result > 0)
				high = mid - 1;
			else
				return mid; // Found
		}

		return NOT_FOUND;
	}

	/**
	 * <p>Search an object in an list of object using a binary search.</p>
	 * <p>Binary search is O(log N) complexity</p>
	 * <p>The list must be sorted from lower to higher</p>
	 * 
	 * @param l the list to search in
	 * @param x the object to search for
	 * @return the index of the object, -1 if not found
	 */
	public static <T extends Comparable<? super T>> int binarySearch(List<T> l, T x)
	{
		int low = 0, high = l.size() - 1;

		while (low <= high)
		{
			int mid = (low + high) / 2;

			int result = l.get(mid).compareTo(x);

			if (result < 0)
				low = mid + 1;
			else if (result > 0)
				high = mid - 1;
			else
				return mid; // Found
		}

		return NOT_FOUND;
	}

	/**
	 * <p>Return the maximum sum of a consecutive sub sequence of the array</p>
	 * <p>For exemple, consider the following array : -2 11, -4, 13, -5, -2.
	 * <br />Then the maximum sub sequence sum is 20 (11, -4, 13).</p>
	 * 
	 *  
	 * @param a an array of integers (positive and negatives)
	 * @return the maximum sub sequence sum
	 */
	public static int maxSubSum(int[] a)
	{
		// Here are others algorithms that are working
		// But that have a higher complexity

		// This is the simple but worst algorithm, complexity O(N³)
		
		/*int maxSum = 0;
		
		for (int i = 0; i < a.length; i++)
		{
			for (int j = i; j < a.length; j++)
			{
				int thisSum = 0;
				
				for (int k = i; k <= j; k++)
					thisSum += a[k];
				
				if (thisSum > maxSum)
					maxSum = thisSum;
			}
		}
		
		return maxSum;*/
		
		// Second one, complexity O(N²)
		
		/*int maxSum = 0;
		for (int i = 0; i < a.length; i++)
		{
			int thisSum = 0;
			for (int j = i; j < a.length; j++)
			{
				thisSum += a[j];
				
				if (thisSum > maxSum)
					maxSum = thisSum;
			}
		}
		
		return maxSum;*/
		
		// Third one, better than the previous one but very complex
		// Complexity O(N log N)
		
		// This algorithm uses the "divide and conquer" strategy
		// We calculate first the max sum of the left half of the array,
		// then on the right half, then we return the max value of the left, 
		// right and the sum of the two at the center
		
		/*public static int maxSumRec(int[] a, int left, int right)
		{
			if (left == right)
			{
				if (a[left] > 0)
					return a[left];
				else
					return 0;
			}
			
			int center = (left + right) / 2;
			int maxLeftSum = maxSumRec(a, left, center);
			int maxRightSum = maxSumRec(a, center + 1, right);
			
			int maxLeftBorderSum = 0, leftBorderSum = 0;
			for (int i = center; i >= left; i--)
			{
				leftBorderSum += a[i];
				if (leftBorderSum > maxLeftBorderSum)
					maxLeftBorderSum = leftBorderSum;
			}
			
			int maxRightBorderSum = 0, rightBorderSum = 0;
			for (int i = center + 1; i <= right; i++)
			{
				rightBorderSum += a[i];
				if (rightBorderSum > maxRightBorderSum)
					maxRightBorderSum = rightBorderSum;
			}
			
			// max3() return the maximum of the 3 values
			return max3(maxLeftSum, maxRightSum, maxLeftBorderSum + maxRightBorderSum);
		}*/

		// This is the best algorithm, complexity O(N)
		
		int maxSum = 0, thisSum = 0;

		for (int j = 0, n = a.length; j < n; j++)
		{
			thisSum += a[j];

			if (thisSum > maxSum)
				maxSum = thisSum;
			else
				thisSum = 0;
		}
		return maxSum;
	}
}