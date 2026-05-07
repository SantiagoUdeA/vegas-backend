import boto3

client = boto3.client('cognito-idp', region_name='us-east-2')
response = client.initiate_auth(
    ClientId='5rdepvqfv3phti5qpknit8pba8',
    AuthFlow='USER_PASSWORD_AUTH',
    AuthParameters={
        'USERNAME': 'carlos.martinez@vegassystem.co',
        'PASSWORD': 'Vegas2025*!'
    }
)
print(response['AuthenticationResult']['IdToken'])
