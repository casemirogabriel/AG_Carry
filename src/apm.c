void apm (pop *population, double averageFunction, double* k, double **vAcumulation, double **violation) {

	int i, j;
	double V[N_CONST], sumConstraints[N_IND], averageFunction_k, a, function, square = 0;

	if(averageFunction > 0)
		averageFunction_k = averageFunction;
	else
		averageFunction_k = -averageFunction;
	
	for (i = 0; i < N_CONST; i++)
		V[i] = 0; 
		
	/// Call to violations function
	violations (population, violation, V, vAcumulation, sumConstraints);

	for (i = 0; i < N_CONST; i++) { 
		V[i] = V[i] / N_IND; 
	}/// change here! what??
	
	/// Calculation of 'k'
	for (i = 0; i < N_CONST; i++) 
		square += pow(V[i], 2);

	for (i = 0; i < N_CONST; i++) {
		if (square != 0)	
			k[i] = (averageFunction_k * V[i]) / square;
		else
			k[i] = 0;
	}

	for (i = 0; i < N_IND; i++){
		function = population->individual[i].fitness; 
		a = 0;

		for (j = 0; j < N_CONST; j++) 
			a += violation[i][j] * k[j];

		if(a!=0) {    
			if(function < averageFunction)
				function = averageFunction;	
			function = function + a;
			population->individual[i].fitness = function;
		}  
	}
}
