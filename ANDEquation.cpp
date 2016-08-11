#include <vector>

using namespace std;

class ANDEquation{
	public:
    int restoreY(vector<int> A);
};

int computeAnd(vector<int> v, int skipIndex){
    int res=-1;
    for(int i=0;i<v.size();i++){
        if (i==skipIndex) continue;
        if (res==-1) res=v[i];
        else{
            res &= v[i];
        }
    }
    return res;
}

int ANDEquation::restoreY(vector<int> A){
    for(int i=0;i<A.size();i++){
        if (A[i]==computeAnd(A, i)) return A[i];
    }
    return -1;
}
