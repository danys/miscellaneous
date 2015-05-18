#include <iostream>

#define maxitems 2000
#define maxsize 2000000

using namespace std;

int values[maxitems];
int sizes[maxitems];
int dp[2][maxsize+1];

/**Solves a Knapsack instance up to a maximum Knapsack size and maximum item size as defined in
***the preprocessor directives. Time complexity: O(nitems*ksize).
***This implementation only stores the results of the current iteration as well as the results
***from the preceding iteration. Space complexity: O(ksize). (ksize dominates maxitems) **/
int solve(int nitems,int ksize)
{
    for(int i=0;i<=ksize;i++) dp[0][i]=0;
    int cindex,previndex;
    for(int i=1;i<=nitems;i++)
    {
        if ((i%2)==1)
        {
            previndex = 1;
            cindex = 0;
        }
        else
        {
            cindex = 1;
            previndex = 0;
        }
        for(int j=0;j<=ksize;j++)
        {
            if (j-sizes[i-1]<0) dp[cindex][j] = dp[previndex][j];
            else
            {
                dp[cindex][j] = max(dp[previndex][j],dp[previndex][j-sizes[i-1]]+values[i-1]);
            }
        }
    }
    if (nitems%2==0) return dp[1][ksize];
    else return dp[0][ksize];
}

int main()
{
    int ksize, nitems;
    cin >> ksize >> nitems;
    for(int z=1;z<=nitems;z++) cin >> values[z-1] >> sizes[z-1];
    cout << "Maximum value = " << solve(nitems,ksize) << endl;
    return 0;
}
