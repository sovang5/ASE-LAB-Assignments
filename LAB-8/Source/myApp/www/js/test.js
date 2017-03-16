
describe('myAppController', function(){
    beforeEach(module('starter'));
    var $control;
    
    beforeEach(inject(function(_$controller_){
        $control = _$controller_;
    }));
    
    describe('username', 'password', function(){
        it('Testing login credentials', function(){
            
            var user = $scope.username();
            var pass = $scope.password();
            expect(user).toEqual('akhi');
            expect(pass).toEqual('akhi');
                })
        });
    });
});