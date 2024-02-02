def encrypt(data: str, key: int):

    ciphertext = ''
    data = data.upper().replace(" ","")
    for char in data:
        ciphertext += chr(((ord(char) - ord('A') + key) % 26) + ord('A'))
    return ciphertext


def decrypt(data: str, key: int):
    
    plaintext = ''
    data = data.upper().replace(" ","")
    for char in data:
        plaintext += chr(((ord(char) - ord('A') - key) % 26) + ord('A'))
    return plaintext


def brute_force_key(plaintext: str, ciphertext: str):
    
    plaintext = plaintext.upper()
    for key in range(0, 26):
        cipher_value = encrypt(plaintext, key)
        if ciphertext.upper() == cipher_value:
            return key
    return "Key not found"



while True:
    try:
        choice = int(input("1. Encryption.\n2. Decryption.\n3. Brute Force.\n4. Exit.\nEnter your choice: "))
    except ValueError:
        print("Please choose one of the following [1, 2, 3, 4]")
        

    if choice == 1:
        while True:
            data = str(input("Enter data: "))
            break
        

        while True:
            try:
                key = int(input("Enter Key: "))
                break
            except ValueError:
                print("\nPlease Enter an integer for Key.\n")

        result = encrypt(data, key)
        print(f"\nEncrypted Message:\n{result}\n")

        

    elif choice == 2:
        while True:
            data = str(input("Enter data: "))
            break
        

        while True:
            try:
                key = int(input("Enter Key: "))
                break
            except ValueError:
                print("\nPlease Enter an integer for Key.\n")

        result = decrypt(data, key)
        print(f"\nDecrypted Message:\n{result}\n")

        

    elif choice == 3:
        while True:
            plaintext = str(input("Enter Plaintext: "))
            break
        

        while True:
            ciphertext = str(input("Enter Ciphertext: "))
            break

        result = brute_force_key(plaintext, ciphertext)
        print(f"\nKey found, Key :{result}\n")

        

    elif choice == 4:
        print("\nExiting Program Exited Successfully.\n")
        break
