#include <string>
#include <math.h>
#include <vector>

using namespace std;

class BearNSWE{
    public:
    	double totalDistance(vector<int> a, string dir);
};

double BearNSWE::totalDistance(vector<int> a, string dir){
    double distance = 0;
    double deltax = 0, deltay = 0;
    for(int i=0;i<dir.size();i++){
        distance += a[i];
        if (dir[i]=='N') deltax += a[i];
        else if (dir[i]=='S') deltax -= a[i];
        else if (dir[i]=='E') deltay += a[i];
        else if (dir[i]=='W') deltay -= a[i];
    }
    distance += sqrt(deltax*deltax+deltay*deltay);
    return distance;
}
