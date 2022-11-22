
mpi4alljava: 
	gcc --shared -Wl,--no-as-needed -fPIC -rdynamic -lmpi mpi4alljava.c -o libmpi4alljava.so -L/opt/mpich/lib -I/opt/mpich/include

clean:
	rm -f libmpi4alljava.so
