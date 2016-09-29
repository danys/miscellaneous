#include <math.h>

using namespace std;

class MagicDiamonds{
	public:
    	long long minimalTransfer(long long x);
};

bool prim(long long x){
	if (x<=1) return false;
    if (x==2) return true;
    if (x%2==0) return false;
    for(int i=3;i<=sqrt(x);i+=2){
    	if ((x!=i) && (x%i==0)) return false;
    }
    return true;
}

long long MagicDiamonds::minimalTransfer(long long x){
    if (x==3) return 3;
    if (prim(x)) return 2;
    else return 1;
}
